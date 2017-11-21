package pl.radomiej.formula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.radomiej.formula.macro.IterationMacro;
import pl.radomiej.formula.macro.LogicMacro;
import pl.radomiej.formula.natives.ArrayPutNativeFunction;
import pl.radomiej.formula.natives.ConsoleNativeFunction;
import pl.radomiej.formula.natives.DateTimeNativeFunction;
import pl.radomiej.formula.natives.ExecNativeFunction;
import pl.radomiej.formula.natives.IsDefinedNativeFunction;
import pl.radomiej.formula.natives.IsUndefinedNativeFunction;
import pl.radomiej.formula.natives.MathNativeFunction;
import pl.radomiej.formula.natives.MathStrategy;
import pl.radomiej.formula.natives.MaxIntegerNativeFunction;
import pl.radomiej.formula.natives.MinIntegerNativeFunction;
import pl.radomiej.formula.natives.NewObjectNativeFunction;
import pl.radomiej.formula.natives.ObjectLenghtNativeFunction;
import pl.radomiej.formula.natives.ReflectionRemoveFieldNativeFunction;
import pl.radomiej.formula.natives.ReturnNativeFunction;
import pl.radomiej.formula.natives.SplitTextNativeFunction;
import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.variables.FormulaNumber;

public class FormulaEngine {
	private Map<String, FormulaFunction> functions = new HashMap<String, FormulaFunction>();
	protected Map<String, FormulaVariable> globalVariables = new HashMap<String, FormulaVariable>();
	private Map<String, NativeFormulaMethod> nativeFunctions = new HashMap<String, NativeFormulaMethod>();
	private FormulaDebug formulaDebuger;

	public FormulaEngine() {
		formulaDebuger = new FormulaDebug();

		nativeFunctions.put("console", new ConsoleNativeFunction());
		
		nativeFunctions.put("exec", new ExecNativeFunction());
		nativeFunctions.put("return", new ReturnNativeFunction());
		nativeFunctions.put("array_put", new ArrayPutNativeFunction());
		nativeFunctions.put("obj_new", new NewObjectNativeFunction());
		nativeFunctions.put("obj_len", new ObjectLenghtNativeFunction());
		nativeFunctions.put("spli_str", new SplitTextNativeFunction());
		nativeFunctions.put("datetime", new DateTimeNativeFunction());
		nativeFunctions.put("is_defined", new IsDefinedNativeFunction());
		nativeFunctions.put("is_undefined", new IsUndefinedNativeFunction());
		
		// REFLECTION
		nativeFunctions.put("reflection_field_remove", new ReflectionRemoveFieldNativeFunction());
		
		//MATH
		nativeFunctions.put("sin", new MathNativeFunction(MathStrategy.SIN, -1, 1));
		nativeFunctions.put("sinh", new MathNativeFunction(MathStrategy.SINH, -1, 1));
		nativeFunctions.put("asin", new MathNativeFunction(MathStrategy.ASIN, -1, 1));
		nativeFunctions.put("cos", new MathNativeFunction(MathStrategy.COS, -1, 1));
		nativeFunctions.put("cosh", new MathNativeFunction(MathStrategy.COSH, -1, 1));
		nativeFunctions.put("acos", new MathNativeFunction(MathStrategy.ACOS, -1, 1));
		nativeFunctions.put("tan", new MathNativeFunction(MathStrategy.TAN, -1, 1));
		nativeFunctions.put("tanh", new MathNativeFunction(MathStrategy.TANH, -1, 1));
		nativeFunctions.put("atan", new MathNativeFunction(MathStrategy.ATAN, -1, 1));
		nativeFunctions.put("atan2", new MathNativeFunction(MathStrategy.ATAN2, -1, 1));
		nativeFunctions.put("ceil", new MathNativeFunction(MathStrategy.CEIL));
		nativeFunctions.put("exp", new MathNativeFunction(MathStrategy.EXP));
		nativeFunctions.put("fabs", new MathNativeFunction(MathStrategy.FABS));
		nativeFunctions.put("floor", new MathNativeFunction(MathStrategy.FLOOR));
		nativeFunctions.put("frac", new MathNativeFunction(MathStrategy.FRAC));
		nativeFunctions.put("int", new MathNativeFunction(MathStrategy.INT));
		nativeFunctions.put("log", new MathNativeFunction(MathStrategy.LOG));
		nativeFunctions.put("log10", new MathNativeFunction(MathStrategy.LOG10));
		nativeFunctions.put("pow", new MathNativeFunction(MathStrategy.POW));
		nativeFunctions.put("sqrt", new MathNativeFunction(MathStrategy.SQRT));
		nativeFunctions.put("max_integer", new MaxIntegerNativeFunction());
		nativeFunctions.put("min_integer", new MinIntegerNativeFunction());
	}

	public boolean containsNativeMethod(String query) {
		if (query.indexOf('(') > 0)
			query = query.substring(0, query.indexOf('('));
		return nativeFunctions.containsKey(query);
	}

	/**
	 * Dodaje natywną funkcje wraz z jej implementacją pod wskazaną nazwą.
	 * 
	 * @param nativeFunctionName
	 *            nazwa wywo�ywanej funkcji z poziomu FORMULI
	 * @param nativeMethodImplementation
	 *            implementacja funkcji natywnej w JAVIE
	 */
	public void addNativeFunctionImplementation(String nativeFunctionName,
			NativeFormulaMethod nativeMethodImplementation) {
		nativeFunctions.put(nativeFunctionName, nativeMethodImplementation);
	}

	/**
	 * Wywołanie natywnej funkcji z podanego query i pamięci lokalnej
	 * 
	 * @param query
	 *            Surowa forma wywołania natywnej funkcji przekazana to
	 *            przetworzenia
	 * @param function
	 *            Pamięć lokalna w której przechowywane są wszystkie zmienne
	 *            lokalne do której wywoływane wyrażenie może mieć dostęp.
	 * @return Wartość wywoływanej funkcji. Typu FormulaVariable.
	 * @throws FormulaException 
	 */
	public FormulaVariable invokeNativeFunction(String query, LocalMemory function) throws FormulaException {
		List<FormulaVariable> arguments = getNativeFunctionArguments(query, function);
		String functionName = getNativeFunctionName(query);

		if (nativeFunctions.containsKey(functionName)) {
			return nativeFunctions.get(functionName).invoke(this, arguments);
		}
		if (globalVariables.containsKey(functionName)
				&& globalVariables.get(functionName).getType() == FormulaVariableType.FUNCTION) {
			FormulaFunction varFunction = (FormulaFunction) globalVariables.get(functionName).getValue();
			varFunction = varFunction.copy();
			return invokeAnonymousFunction(varFunction);
		}
		throw new FormulaException("Brak natywnej funkcji o nazwie: " + functionName);
	}

	private String getNativeFunctionName(String query) {
		int index = query.indexOf('(');
		if (index >= 0)
			query = query.substring(0, index);
		return query;
	}

	private List<FormulaVariable> getNativeFunctionArguments(String query, LocalMemory function) throws FormulaException {
		query = FormulaLexyconHelper.prepareQuery(query);
		List<FormulaVariable> arguments = new ArrayList<FormulaVariable>();
		int index = query.indexOf('(');
		if (index >= 0) {
			query = query.substring(index + 1, query.length() - 1);
			// Zabezpieczenie dla pustego stringa
			if (query.isEmpty())
				return arguments;

			// String[] argsTab = query.split(",");
			String[] argsTab = FormulaLexyconHelper.split(query, ",");
			for (String arg : argsTab) {
				FormulaLine line = new FormulaLine(arg);
				FormulaVariable argVar = invokeLine(line, function);
				arguments.add(argVar);
			}
		}

		return arguments;
	}

	public FormulaVariable invoke(FormulaInstruction instructionToInvoke, LocalMemory localMemory)
			throws FormulaException {
		if (instructionToInvoke instanceof FormulaLine) {
			return invokeLine((FormulaLine) instructionToInvoke, localMemory);
		} else if (instructionToInvoke instanceof LogicMacro) {
			return invokeLogicMacro((LogicMacro) instructionToInvoke, localMemory);
		} else if (instructionToInvoke instanceof IterationMacro) {
			return invokeIterationMacro((IterationMacro) instructionToInvoke, localMemory);
		} else if (instructionToInvoke instanceof FormulaMacro) {
			return invokeMacro((FormulaMacro) instructionToInvoke, localMemory);
		} else {
			throw new FormulaException(
					"Niewspierany typ instrukcji do wykonania: " + instructionToInvoke.getClass().getSimpleName());
		}
	}

	/**
	 * Wykonuje makra zawarte w przekazanej funkcji. Operacje s� wykonywane na
	 * przekazanym obiekcie.
	 * 
	 * @param function
	 *            Obiekt funkcji do wykonania
	 * @return warto�� zwr�con� przez funkcje
	 */
	public FormulaVariable invokeAnonymousFunction(FormulaFunction function) {
		
		formulaDebuger.insideScope(function);
		FormulaVariable lastResult = FormulaVariable.getUndefined();
		for (FormulaInstruction instruction : function.getMacros()) {
			try {
				lastResult = invoke(instruction, function);
			} catch (FormulaException ex) {
				formulaDebuger.proccessException(ex);
				formulaDebuger.outScope(function);
				return new FormulaVariable();
			}
			if (lastResult.getType() == FormulaVariableType.RETURN) {
				lastResult = (FormulaVariable) lastResult.getValue();
				break;
			}
		}

		formulaDebuger.outScope(function);
		return lastResult;
	}

	/**
	 * Wykonuje makra z funkcji globalnej zarejestrowanej w silniku. Każde
	 * wywołanie funkcji tworzy jej osobną kopie.
	 * 
	 * @param functionName example: "client@main"
	 * @return wartość zwrócona przez funkcje
	 */
	@SuppressWarnings("unchecked")
	public FormulaVariable invokeFunction(String functionName) {
		return invokeFunction(functionName, Collections.EMPTY_LIST);
	}

	/**
	 * Wykonuje makra z funkcji globalnej zarejestrowanej w silniku. Ka�de
	 * wywo�anie funkcji tworzy jej osobn� kopie.
	 * 
	 * @param functionName
	 *            nazwa wykonywanej metody
	 * @param args
	 *            argumenty przekazane do wywo�ywanej metody
	 * @return warto�� zwr�con� przez funkcje
	 */
	public FormulaVariable invokeFunction(String functionName, List<FormulaVariable> args) {
		FormulaFunction function = functions.get(functionName);
		if(function == null){
			System.err.println("Brak funkcji: " + functionName);
		}
		return invokeAnonymousFunction(function.copy(args));
	}

	private FormulaVariable invokeLine(FormulaLine line, LocalMemory localMemory) throws FormulaException {
		FormulaLeaf master = line.getOperationLeaf();
		FormulaVariable result = FormulaLeafHelper.getVariableFromLeaf(master, this, localMemory);
		return result;
	}

	private FormulaVariable invokeMacro(FormulaMacro macro, LocalMemory localMemory) {
		macro.setParent(localMemory);
		return invokeInstructions(macro.getInstructions(), macro);
	}

	private FormulaVariable invokeInstructions(List<FormulaInstruction> instructions, LocalMemory localMemory) {
		FormulaVariable lastResult = new FormulaVariable();
		formulaDebuger.insideScope(localMemory);
		for (FormulaInstruction instruction : instructions) {
			formulaDebuger.currentInstruction(localMemory, instruction);
			try {
				lastResult = invoke(instruction, localMemory);
			} catch (FormulaException ex) {
				formulaDebuger.proccessException(ex);
				formulaDebuger.outScope(localMemory);
				return new FormulaVariable();
			}
			if (lastResult.getType() == FormulaVariableType.RETURN) {
				break;
			}
		}
		
		formulaDebuger.outScope(localMemory);
		return lastResult;
	}

	private FormulaVariable invokeLogicMacro(LogicMacro logicMacro, LocalMemory localMemory) throws FormulaException {
		logicMacro.setParent(localMemory);
		FormulaVariable lastResult = new FormulaVariable();

		lastResult = invokeInstructions(logicMacro.getCondition(), logicMacro);
		if (lastResult.getType() != FormulaVariableType.NUMBER && lastResult.getValue() instanceof FormulaNumber) {
			throw new FormulaException("Ostatnia zmienna condition powinna zwrócić wartość typu NUMBER, zwrócono: "
					+ lastResult.getType());
		}
		int conditionResult = (Integer) ((FormulaNumber) lastResult.getValue()).getInt();
		if (conditionResult != 0) {
			lastResult = invokeInstructions(logicMacro.getInstructions(), logicMacro);
		} else {
			lastResult = invokeInstructions(logicMacro.getNegative(), logicMacro);
		}

		return lastResult;
	}

	// TODO dodać wsparcie dla foreach dla OBJECT i ARRAY
	private FormulaVariable invokeIterationMacro(IterationMacro iterationMacro, LocalMemory localMemory)
			throws FormulaException {
		iterationMacro.setParent(localMemory);
		int stepStart = 1;
		int stepStop = Integer.MAX_VALUE;

		FormulaLeaf initLeaf = ((FormulaLine) iterationMacro.getInitLoop().get(0)).getOperationLeaf();

		FormulaLeaf rangeQuery = null;
		String localIteratorVariableName = null;

		if (initLeaf.getType() != FormulaOperationType.ATTRIBUTION) {
			rangeQuery = initLeaf;
		} else {
			rangeQuery = initLeaf.getRight();
			localIteratorVariableName = initLeaf.getLeft().getQuery();
			iterationMacro.setLoopCounterName(localIteratorVariableName);
		}

		String[] minAndMax = rangeQuery.getQuery().split("\\.\\.");
		if (!minAndMax[0].equals("")) {
			FormulaVariable stepStartVar = FormulaLeafHelper.getVariableFromQuery(minAndMax[0], this, iterationMacro);
			stepStart = stepStartVar.getValueLikeNumber().getInt();
		}
		FormulaVariable stopStartVar = FormulaLeafHelper.getVariableFromQuery(minAndMax[1], this, iterationMacro);
		stepStop = stopStartVar.getValueLikeNumber().getInt();

		FormulaVariable loopCounter = null;
		if (localIteratorVariableName != null && !localIteratorVariableName.isEmpty()) {
			loopCounter = iterationMacro.getOrCreateLocalVariable(initLeaf.getLeft().getQuery().trim());
		} else {
			loopCounter = FormulaVariable.getOne();
		}

		iterationMacro.setLoopCounter(loopCounter);
		loopCounter.setFullValue(stepStart);

		FormulaVariable lastResult = new FormulaVariable();
		for (int i = stepStart; i <= stepStop; i = loopCounter.getValueLikeNumber().getInt()) {

			if (conditionIsValid(iterationMacro.getCondition(), iterationMacro)) {
				lastResult = invokeInstructions(iterationMacro.getInstructions(), iterationMacro);
				i = loopCounter.getValueLikeNumber().getInt();
				loopCounter.setFullValue(++i);
			} else {
				break;
			}
		}

		return lastResult;
	}

	private boolean conditionIsValid(List<FormulaInstruction> list, IterationMacro iterationMacro) throws FormulaException {
		if (list.size() == 0)
			return true; // Brak sekcji condition do wykonania
		FormulaVariable lastResult = invokeInstructions(iterationMacro.getCondition(), iterationMacro);
		if (lastResult.getType() != FormulaVariableType.NUMBER) {
			throw new FormulaException("Ostatnia zmienna condition powinna zwr�ci� warto�� typu NUMBER, zwr�cono: "
					+ lastResult.getType());
		}
		int conditionResult = lastResult.getValueLikeNumber().getInt();
		if (conditionResult != 0) {
			return true;
		}

		return false;
	}

	public FormulaVariable getGlobalVariable(String query) {
		return globalVariables.get(query);
	}

	public FormulaVariable getOrCreateGlobalVariable(String query) {
		if (!globalVariables.containsKey(query)) {
			globalVariables.put(query, FormulaVariable.getUndefined());
		}
		return getGlobalVariable(query);
	}

	public void addFunction(String functionName, FormulaFunction function) {
		functions.put(functionName, function);
	}

	public FormulaDebug getDebug() {
		return formulaDebuger;
	}

}

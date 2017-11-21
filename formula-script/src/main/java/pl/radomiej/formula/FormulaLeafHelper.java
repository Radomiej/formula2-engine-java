package pl.radomiej.formula;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import pl.radomiej.formula.operations.FormulaOperation;
import pl.radomiej.formula.operations.FormulaOperationType;
import pl.radomiej.formula.operations.LogicOperation;
import pl.radomiej.formula.parser.FormulaFunctionParser;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

public class FormulaLeafHelper {
	public static FormulaVariable getVariableFromLeaf(FormulaLeaf leaf, FormulaEngine engine, LocalMemory local) throws FormulaException {
		String query = leaf.getQuery();
		query = FormulaLexyconHelper.prepareQuery(query);
		if (query.isEmpty())
			return FormulaVariable.getUndefined();
		if (leaf.isSimpleQuery()) {
			return getVariableFromQuery(query, engine, local);
		} else {// Złożone query, czyli query wykonujące jakąś operacje
			FormulaOperation operation = leaf.getType().getOperationExecuter();
			FormulaVariable left = getVariableFromLeaf(leaf.getLeft(), engine, local);

			// Wsparcie dla sprawdzania tylko jednej strony równania w AND
			boolean checkRight = true;
			if (leaf.getType() == FormulaOperationType.AND && LogicOperation.getLogicValue(left) <= 0) {
				checkRight = false;
				FormulaVariable result = new FormulaVariable(0);
				return result;
			}
			if (leaf.getType() == FormulaOperationType.OR && LogicOperation.getLogicValue(left) > 0) {
				checkRight = false;
				FormulaVariable result = new FormulaVariable(1);
				return result;
			}
			FormulaVariable right = left;
			if (checkRight)
				right = getVariableFromLeaf(leaf.getRight(), engine, local);
			return operation.invoke(left, right);
		}
	}

	private static FormulaVariable ShortestObject(String query, FormulaEngine engine, LocalMemory local) {
		FormulaVariable result = new FormulaVariable();
		if(query.charAt(0) == '[' && query.charAt(query.length() - 1)== ']'){
			FormulaArray formulaArray = new FormulaArray();
			result.setFullValue(formulaArray);
			
			query = query.substring(1, query.length() - 1);
			String[] parts = FormulaLexyconHelper.split(query, ",");
			for(String part : parts){
				try {
					FormulaVariable element = getVariableFromQuery(part, engine, local);
					formulaArray.addVariable(element);
				} catch (FormulaException e) {
					e.printStackTrace();
				}
			}
		}
		else if(query.charAt(0) == '{' && query.charAt(query.length() - 1)== '}'){
			FormulaObject formulaObject = new FormulaObject();
			result.setFullValue(formulaObject);
			
			query = query.substring(1, query.length() - 1);
			String[] parts = FormulaLexyconHelper.split(query, ",");
			
			for(String part : parts){
//				System.out.println("part: " + part);
//				String[] keyValue = part.split(":");
				String[] keyValue = FormulaLexyconHelper.split(part, ":");
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				
				try {
					FormulaVariable field = getVariableFromQuery(value, engine, local);
					formulaObject.setField(key, field);
				} catch (FormulaException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	public static boolean isShortestConstructor(String query) {
		if(query.charAt(0) == '[' && query.charAt(query.length() - 1)== ']'){
			return true;
		}
		if(query.charAt(0) == '{' && query.charAt(query.length() - 1)== '}'){
			return true;
		}
		return false;
	}

	public static FormulaVariable getVariableFromQuery(String query, FormulaEngine engine, LocalMemory local) throws FormulaException {
		query = FormulaLexyconHelper.prepareQuery(query);
		if (query.isEmpty())
			return FormulaVariable.getUndefined();
		if(isShortestConstructor(query)){
			return ShortestObject(query, engine, local);
		}
		if (FormulaLexyconHelper.isConstans(query)) {
			FormulaVariable variable = FormulaVariable.getUndefined();
			if (FormulaLexyconHelper.isConstansString(query)) {
				variable.setType(FormulaVariableType.STRING);
				variable.setValue(FormulaLexyconHelper.getStringValueFromQuery(query));
			} else {
				variable.setType(FormulaVariableType.NUMBER);
				variable.setValue(new FormulaNumber(query));
			}
			return variable;
		} else if (FormulaLexyconHelper.isSimpleAnoymousFunction(query)
				&& FormulaLexyconHelper.containsBrackets(query)) {
			FormulaFunctionParser formulaFunctionParser = new FormulaFunctionParser(query, true);
			return engine.invokeAnonymousFunction(formulaFunctionParser.getFunction());
		} else if (FormulaLexyconHelper.isSimpleAnoymousFunction(query)) {
			// Is simple anoymous function definition
			FormulaFunctionParser formulaFunctionParser = new FormulaFunctionParser(query, true);
			FormulaVariable variable = new FormulaVariable();
			variable.setType(FormulaVariableType.FUNCTION);
			variable.setValue(formulaFunctionParser.getFunction());
			return variable;
		} else if (engine.containsNativeMethod(query)) { // Is
			return engine.invokeNativeFunction(query, local);
		} else if (FormulaLexyconHelper.containsBrackets(query)) {
			String tempQuery = query.substring(0, query.indexOf('('));
			FormulaVariable variable = getVariableFromQuery(tempQuery, engine, local);
			if (variable.getType() == FormulaVariableType.FUNCTION && variable.getValue() instanceof FormulaFunction) {
				return engine.invokeAnonymousFunction((FormulaFunction) variable.getValue());
			} else {
				throw new RuntimeException("Zmienna nie jest funkcj�: " + query);
			}
		} else if (query.contains("[") && query.charAt(query.length() - 1) == ']') {
			return getVariableFromArray(query, engine, local);
		} else if (query.contains(".")) {
			return getVariableFromObject(query, engine, local);
		} else if (query.charAt(0) == '_' && (query.length() == 1 || query.charAt(1) != '_')) {
			return local.getLocalVariable(query);
		} else {
			return engine.getOrCreateGlobalVariable(query);
		}
	}

	// TODO doda� wspracie dla sytuacji wyj�tkowych
	private static FormulaVariable getVariableFromArray(String query, FormulaEngine engine, LocalMemory local) throws FormulaException {
		String varQuery = query.substring(0, query.lastIndexOf('['));
		String varIndex = query.substring(query.lastIndexOf('[') + 1, query.length() - 1);
		FormulaVariable variable = getVariableFromQuery(varQuery, engine, local);

		try{
			return getArrayElement(varIndex, variable, engine, local);
		}catch(FormulaException ex ){
			ex.setQuery(query);
			throw ex;
		}catch(NullPointerException e){
			FormulaException ex = new FormulaException(e.getMessage());
			ex.setQuery(query);
			throw ex;
		}
	}

	private static FormulaVariable getArrayElement(String queryIndex, FormulaVariable parentObject,
			FormulaEngine engine, LocalMemory local) throws FormulaException {
		FormulaVariable queryVar = getVariableFromQuery(queryIndex, engine, local);

		if (queryVar.getType() == FormulaVariableType.NUMBER && parentObject.getType() == FormulaVariableType.ARRAY) {
			FormulaArray array = (FormulaArray) parentObject.getValue();
			int index = queryVar.getValueLikeNumber().getInt();
			return array.getVariable(index);
		} else if (queryVar.getType() == FormulaVariableType.NUMBER
				&& parentObject.getType() == FormulaVariableType.OBJECT) {
			FormulaObject object = (FormulaObject) parentObject.getValue();
			int index = queryVar.getValueLikeNumber().getInt();
			return object.getField(index);
		} else {
			FormulaObject object = (FormulaObject) parentObject.getValue();
			return object.getField(queryVar.getValue().toString());
		}

		// if (NumberUtils.isCreatable(varIndex) && parentObject.getType() ==
		// FormulaVariableType.ARRAY) {
		// FormulaArray array = (FormulaArray) parentObject.getValue();
		// int index = Integer.parseInt(varIndex);
		// return array.getVariable(index);
		// } else if (NumberUtils.isCreatable(varIndex) &&
		// parentObject.getType() == FormulaVariableType.OBJECT) {
		// FormulaObject object = (FormulaObject) parentObject.getValue();
		// int index = Integer.parseInt(varIndex);
		// return object.getField(index);
		// } else {
		// FormulaObject object = (FormulaObject) parentObject.getValue();
		// varIndex = varIndex.substring(varIndex.indexOf('\'') + 1,
		// varIndex.length() - 1);
		// return object.getField(varIndex);
		// }
	}

	private static FormulaVariable getVariableFromObject(String query, FormulaEngine engine, LocalMemory local) throws FormulaException {
		String[] pathObject = query.split("\\.");

		FormulaVariable parentVariable = getVariableFromQuery(pathObject[0], engine, local);
		for (int i = 1; i < pathObject.length; i++) {
			String currentPath = pathObject[i];
			if (parentVariable.getType() == FormulaVariableType.OBJECT) {
				parentVariable = parentVariable.getValueLikeObject().getField(currentPath);
			} else if (parentVariable.getType() == FormulaVariableType.ARRAY) {
				parentVariable = getArrayElement(currentPath, parentVariable, engine, local);
			} 
		}
		return parentVariable;
	}
}

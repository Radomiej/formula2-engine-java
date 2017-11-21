package pl.radomiej.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.radomiej.formula.variables.FormulaArray;

public class FormulaFunction implements LocalMemory {
	private List<FormulaVariable> arguments = new ArrayList<FormulaVariable>();
	private FormulaArray argumentsArray = new FormulaArray();

	private Map<String, FormulaVariable> localVariables = new HashMap<String, FormulaVariable>();
	private List<FormulaMacro> macros = new LinkedList<FormulaMacro>();
	private String name = "anonymous";

	public FormulaFunction() {
//		System.out.println("create function");
		FormulaVariable arrayVariable = new FormulaVariable();
		arrayVariable.setFullValue(argumentsArray);

		localVariables.put("_", arrayVariable);
	}

	public FormulaVariable getLocalVariable(String query) {
		return getOrCreateLocalVariable(query);
	}

	public boolean existLocalVariable(String query) {
		return localVariables.containsKey(query);
	}

	public FormulaVariable getOrCreateLocalVariable(String query) {
		if (!localVariables.containsKey(query)) {
			localVariables.put(query, FormulaVariable.getUndefined());
		}
		return localVariables.get(query);
	}

	public List<FormulaMacro> getMacros() {
		return macros;
	}

	public void setMacros(List<FormulaMacro> macros) {
		this.macros = macros;
	}

	public void addMacro(FormulaMacro macro) {
		macro.copy().setParent(this);
		macros.add(macro);
	}

	public FormulaFunction copy() {
		FormulaFunction function = new FormulaFunction();
		function.name = name;
		for(FormulaMacro macro : macros){
			FormulaMacro macroCopy = macro.copy();
			macroCopy.setParent(function);
			function.macros.add(macroCopy);
		}
		return function;
	}

	public FormulaFunction copy(List<FormulaVariable> args) {
		FormulaFunction function = copy();
		for(FormulaVariable arg : args){
			function.addArgument(arg);
		}
		return function;
	}

	public void addArgument(FormulaVariable formulaVariable) {
		argumentsArray.addVariable(formulaVariable);
		try {
			prepareLetterShortestArgumentsShortcut();
		} catch (FormulaException e) {
			e.printStackTrace();
		}
	}

	//TODO optymalizacja do istniej¹cych liter i iloœci argumentów
	private void prepareLetterShortestArgumentsShortcut() throws FormulaException {
		for (int l = 97; l <= 122; l++) {
			char letter = (char) l;
			String letterArgName =  "_" + letter; 
			localVariables.remove(letterArgName);
			
			int index = (l - 97) + 1;
			if(index <= argumentsArray.getSize()){
				localVariables.put(letterArgName, argumentsArray.getVariable(index));
			}
		}
	}

	public List<FormulaVariable> getArguments() {
		return new ArrayList<FormulaVariable>(arguments);
	}

	public void setArguments(List<FormulaVariable> arguments) {
		this.arguments = arguments;
		argumentsArray.clear();
		for (FormulaVariable arg : arguments) {
			argumentsArray.addVariable(arg);
		}
		try {
			prepareLetterShortestArgumentsShortcut();
		} catch (FormulaException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "FormulaFunction [" + (arguments != null ? "arguments=" + arguments + ", " : "")
				+ (argumentsArray != null ? "argumentsArray=" + argumentsArray + ", " : "")
				+ (localVariables != null ? "localVariables=" + localVariables : "") + "]";
	}

	public String getName() {
		return name ;
	}

	public void setName(String name) {
		this.name = name;
	}

}

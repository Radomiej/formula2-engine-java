package pl.radomiej.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO implementacja i wsparcie dla debugowania
public class FormulaDebug {

	private List<LocalMemory> stack = new ArrayList<LocalMemory>();
	private Map<LocalMemory, FormulaInstruction> currentInstructions = new HashMap<LocalMemory, FormulaInstruction>();
	
	public String getCurrentStack() {
		return "stack: " + System.lineSeparator() + getStackInfo();
	}
	
	private String getStackInfo() {
		StringBuilder builder = new StringBuilder();
		for(LocalMemory memory : stack){
			builder.append(memory.getClass().getSimpleName());
			if(memory instanceof FormulaFunction){
				FormulaFunction formulaFunction = (FormulaFunction) memory;
				builder.append(": " + formulaFunction.getName());
			}else if(memory instanceof FormulaMacro){
				FormulaInstruction currentInstruction = currentInstructions.get(memory);
				builder.append(": " + currentInstruction.toString());
			}
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	public void insideScope(LocalMemory function) {
		stack.add(function);
	}

	public void currentInstruction(LocalMemory localMemory, FormulaInstruction instruction) {
		currentInstructions.put(localMemory, instruction); 		
	}

	public void outScope(LocalMemory function) {
		LocalMemory lastFunction = stack.get(stack.size()-1);
		if(lastFunction != function){
			System.err.println("Zgubiony scope!");
		}
		stack.remove(stack.size()-1);
	}

	@Override
	public String toString() {
		return "FormulaDebug [" + (stack != null ? "stack=" + stack : "") + "]";
	}

	public void proccessException(FormulaException ex) {
		System.err.println("query: " + ex.getQuery() + " " + getCurrentStack());
		ex.printStackTrace();
	}

	
	
	

}

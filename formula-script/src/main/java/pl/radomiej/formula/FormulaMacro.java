package pl.radomiej.formula;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FormulaMacro implements FormulaInstruction, LocalMemory {
	private Map<String, FormulaVariable> localVariables = new HashMap<String, FormulaVariable>();
	private List<FormulaInstruction> instructions = new LinkedList<FormulaInstruction>();
	protected LocalMemory parentMemory;

	public FormulaVariable getLocalVariable(String query) {
		return parentMemory.getLocalVariable(query);
	}

	public FormulaVariable getOrCreateLocalVariable(String query) {
		if (!localVariables.containsKey(query)) {
			if (parentMemory != null && parentMemory.existLocalVariable(query)) {
				return parentMemory.getLocalVariable(query);
			}
			localVariables.put(query, FormulaVariable.getUndefined());
		}
		return localVariables.get(query);
	}

	public boolean existLocalVariable(String query) {
		return localVariables.containsKey(query) || parentMemory.existLocalVariable(query);
	}

	public List<FormulaInstruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<FormulaInstruction> instructions) {
		this.instructions = instructions;
	}

	public void addInstruction(FormulaInstruction instruction) {
		instructions.add(instruction);
	}

	@Override
	public String toString() {
		final int maxLen = 5;
		return "FormulaMacro ["
				+ (localVariables != null ? "localVariables=" + toString(localVariables.entrySet(), maxLen) + ", " : "")
				+ (instructions != null ? "instructions=" + toString(instructions, maxLen) + ", " : "")
				+ (parentMemory != null ? "parentMemory=" + parentMemory : "") + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Kopiuje obiekt macro
	 * 
	 * @return zwraca kopiê macro bez lokalnych zmiennych.
	 */
	public FormulaMacro copy() {
		FormulaMacro cop = new FormulaMacro();
//		cop.setParent(parentMemory);
		for (FormulaInstruction instruction : instructions) {
			cop.instructions.add(instruction.copy());
		}
		return cop;
	}

	public void setParent(LocalMemory parentMemory) {
		this.parentMemory = parentMemory;
	}

}

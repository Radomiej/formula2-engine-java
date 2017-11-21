package pl.radomiej.formula.macro;

import java.util.ArrayList;
import java.util.List;

import pl.radomiej.formula.FormulaInstruction;
import pl.radomiej.formula.FormulaMacro;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.LocalMemory;

public class LogicMacro extends FormulaMacro implements LocalMemory {
	private List<FormulaInstruction> condition = new ArrayList<FormulaInstruction>();
	private List<FormulaInstruction> negative = new ArrayList<FormulaInstruction>();

	@Override
	public FormulaVariable getLocalVariable(String query) {
		return super.getOrCreateLocalVariable(query);
	}
	
	public List<FormulaInstruction> getCondition() {
		return condition;
	}

	public void setCondition(List<FormulaInstruction> condition) {
		this.condition = condition;
	}

	public List<FormulaInstruction> getNegative() {
		return negative;
	}

	public void setNegative(List<FormulaInstruction> negative) {
		this.negative = negative;
	}

	@Override
	public String toString() {
		final int maxLen = 5;
		return "LogicMacro ["
				+ (condition != null ? "condition=" + condition.subList(0, Math.min(condition.size(), maxLen)) + ", "
						: "")
				+ (negative != null ? "negative=" + negative.subList(0, Math.min(negative.size(), maxLen)) : "") + "]";
	}
	
	@Override
	public FormulaMacro copy() {
		LogicMacro cop = new LogicMacro();
//		cop.setParent(parentMemory);
		for (FormulaInstruction instruction : getInstructions()) {
			cop.getInstructions().add(instruction.copy());
		}
		for (FormulaInstruction instruction : negative) {
			cop.negative.add(instruction.copy());
		}
		for (FormulaInstruction instruction : condition) {
			cop.condition.add(instruction.copy());
		}
		return cop;
	}

}

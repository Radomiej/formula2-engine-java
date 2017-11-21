package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;

public enum InstructionOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		return right;
	}
}

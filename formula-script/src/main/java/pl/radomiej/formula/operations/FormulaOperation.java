package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;

public interface FormulaOperation {
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right);
}

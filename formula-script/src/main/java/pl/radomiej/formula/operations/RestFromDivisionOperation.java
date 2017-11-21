package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaComparableHelper;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum RestFromDivisionOperation implements FormulaOperation {
	INSTANCE;

	// TODO dodać implementacje dla innych przypadków niż liczby
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			long leftNumber = left.getValueLikeNumber().getLong();
			long rightNumber = right.getValueLikeNumber().getLong();
			
			int restOfDivide = (int) (leftNumber % rightNumber);
			return new FormulaVariable(restOfDivide);
		}
		return new FormulaVariable(0);
	}
}

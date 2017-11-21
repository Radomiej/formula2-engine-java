package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaComparableHelper;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum EqualsLessOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		if (left.getType() != right.getType()) {
			throw new UnsupportedOperationException(
					"Operacja por�wnania nie jest dozwolona dla r�nych typ�w: " + left + ", " + right);
		}

		int comparator = FormulaComparableHelper.INSTANCE.compareFormulaVariables(left, right);
		FormulaVariable result = new FormulaVariable();

		if (comparator <= 0) {
			result.setFullValue(1);
		} else {
			result.setFullValue(0);
		}

		return result;
	}
}

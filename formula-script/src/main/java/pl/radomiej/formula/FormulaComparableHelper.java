package pl.radomiej.formula;

import org.apache.commons.lang3.StringUtils;

import pl.radomiej.formula.variables.FormulaNumber;

public enum FormulaComparableHelper {
	INSTANCE;
	public int compareFormulaVariables(FormulaVariable left, FormulaVariable right) {
		if (left.getType() != right.getType()) {
			throw new UnsupportedOperationException(
					"Operacja porównania nie jest dozwolona dla ró¿nych typów: " + left + ", " + right);
		}

		int comparator = 0;

		if (left.getType() == FormulaVariableType.NUMBER) {
			FormulaNumber numberL = (FormulaNumber) left.getValue();
			FormulaNumber numberR = (FormulaNumber) right.getValue();
			comparator = numberL.getBigDecimalValue().compareTo(numberR.getBigDecimalValue());
		} else if (left.getType() == FormulaVariableType.STRING) {
			comparator = StringUtils.compare(left.getValue().toString(), right.getValue().toString());
		} else {
			throw new UnsupportedOperationException(
					"Operacja porównania nie jest dozwolona dla typu: " + left.getType());
		}
		return comparator;
	}
}

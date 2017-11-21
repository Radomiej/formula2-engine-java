package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum AttributionMultiplicationOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			multiplicationNumber(left, right, result);
			left.setFull(result);
		} else {
			throw new RuntimeException("Niewspierany typ dodawania: left: " + left + " right: " + right);
		}
		return result;
	}

	private void multiplicationNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.multiply((FormulaNumber) right.getValue()));
	}
}

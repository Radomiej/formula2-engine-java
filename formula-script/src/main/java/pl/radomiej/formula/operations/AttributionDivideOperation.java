package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum AttributionDivideOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			divideNumber(left, right, result);
			left.setFull(result);
		} else {
			throw new RuntimeException("Niewspierany typ dzielenia: left: " + left + " right: " + right);
		}
		return result;
	}

	private void divideNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.divide((FormulaNumber) right.getValue()));
	}
}

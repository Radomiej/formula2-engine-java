package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum AttributionSubtractionOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			subtractNumber(left, right, result);
			left.setFull(result);
		} else {
			throw new RuntimeException("Niewspierany typ odejmowania: left: " + left + " right: " + right);
		}
		return result;
	}

	private void subtractNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.subtract((FormulaNumber) right.getValue()));
	}
}

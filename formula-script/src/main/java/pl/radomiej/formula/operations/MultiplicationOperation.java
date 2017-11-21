package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum MultiplicationOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			multiplicationNumber(left, right, result);
		} else if (left.getType() == right.getType() && left.getType() == FormulaVariableType.STRING) {
			multiplicationString(left, right, result);
		} else if (left.getType() == FormulaVariableType.NUMBER && right.getType() == FormulaVariableType.STRING) {
			leftNumberRightString(left, right, result);
		} else if (left.getType() == FormulaVariableType.STRING && right.getType() == FormulaVariableType.NUMBER) {
			leftStringRightNumber(left, right, result);
		} else {
			throw new RuntimeException("Niewspierany typ dodawania: left: " + left + " right: " + right);
		}
		return result;
	}

	private void leftStringRightNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		String text = (String) left.getValue();
		int repliCount = right.getValueLikeNumber().getInt();
		StringBuilder builder = new StringBuilder(text);
		for (int i = 1; i < repliCount; i++) {
			builder.append(text);
		}
		result.setFullValue(builder.toString());

	}

	private void leftNumberRightString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		String text = (String) right.getValue();
		int repliCount = left.getValueLikeNumber().getInt();
		StringBuilder builder = new StringBuilder(text);
		for (int i = 1; i < repliCount; i++) {
			builder.append(text);
		}
		result.setFullValue(builder.toString());
	}

	private void multiplicationString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		String text = (String) left.getValue();
		String search = (String) right.getValue();
		int index = text.indexOf(search);
		index++;

		result.setFullValue(index);
	}

	private void multiplicationNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.multiply((FormulaNumber) right.getValue()));
	}
}

package pl.radomiej.formula.operations;

import java.util.List;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;

public enum SubtractionOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			subtractNumber(left, right, result);
		} else if (left.getType() == right.getType() && left.getType() == FormulaVariableType.STRING) {
			subtractString(left, right, result);
		} else if (left.getType() == FormulaVariableType.NUMBER && right.getType() == FormulaVariableType.STRING) {
			leftNumberRightString(left, right, result);
		} else if (left.getType() == FormulaVariableType.STRING && right.getType() == FormulaVariableType.NUMBER) {
			leftStringRightNumber(left, right, result);
		} else if (left.getType() == FormulaVariableType.NUMBER && right.getType() == FormulaVariableType.ARRAY) {
			leftNumberRightArray(left, right, result);
		} else if (left.getType() == FormulaVariableType.ARRAY && right.getType() == FormulaVariableType.NUMBER) {
			leftArrayRightNumber(left, right, result);
		} else {
			throw new RuntimeException("Niewspierany typ odejmowania: left: " + left + " right: " + right);
		}
		return result;
	}

	private void leftArrayRightNumber(FormulaVariable arrayVar, FormulaVariable numberVar, FormulaVariable result) {
		int value = numberVar.getValueLikeNumber().getInt();
		FormulaArray array = arrayVar.getValueLikeArray();
		
		List<FormulaVariable> view = array.getElements().subList(0,  array.getSize() - value);
		
		FormulaArray newArray = new FormulaArray();
		for(FormulaVariable varToAdd : view){
			if(varToAdd.getType() == FormulaVariableType.DATE || varToAdd.getType() == FormulaVariableType.DATE_TIME || varToAdd.getType() == FormulaVariableType.NUMBER || varToAdd.getType() == FormulaVariableType.STRING || varToAdd.getType() == FormulaVariableType.TIME){
				varToAdd = new FormulaVariable(varToAdd);
			}
			newArray.addVariable(varToAdd);
		}
		result.setFullValue(newArray);		
	}

	private void leftStringRightNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
//		throw new RuntimeException("Niewspierany typ dodawania: left: " + left + " right: " + right);
		int value = right.getValueLikeNumber().getInt();
		String text = left.getValue().toString().substring(0, left.getValue().toString().length() - value);
		result.setFullValue(text);
	}

	private void leftNumberRightArray(FormulaVariable numberVar, FormulaVariable arrayVar, FormulaVariable result) {
		int value = numberVar.getValueLikeNumber().getInt();
		FormulaArray array = arrayVar.getValueLikeArray();
		List<FormulaVariable> view = array.getElements().subList(value, array.getSize());
		
		FormulaArray newArray = new FormulaArray();
		for(FormulaVariable varToAdd : view){
			if(varToAdd.getType() == FormulaVariableType.DATE || varToAdd.getType() == FormulaVariableType.DATE_TIME || varToAdd.getType() == FormulaVariableType.NUMBER || varToAdd.getType() == FormulaVariableType.STRING || varToAdd.getType() == FormulaVariableType.TIME){
				varToAdd = new FormulaVariable(varToAdd);
			}
			newArray.addVariable(varToAdd);
		}
		result.setFullValue(newArray);
	}

	private void leftNumberRightString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
//		throw new RuntimeException("Niewspierany typ dodawania: left: " + left + " right: " + right);
		int value = left.getValueLikeNumber().getInt();
		String text = right.getValue().toString().substring(value);
		result.setFullValue(text);
	}

	private void subtractString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		throw new RuntimeException("Niewspierany typ odejmowania: STRING - STRING ");
	}

	private void subtractNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.subtract((FormulaNumber) right.getValue()));
	}
}

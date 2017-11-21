package pl.radomiej.formula.operations;

import java.util.List;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;

public enum AddOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			addNumber(left, right, result);
		} else if (left.getType() == right.getType() && left.getType() == FormulaVariableType.STRING) {
			addString(left, right, result);
		} else if (left.getType() == FormulaVariableType.NUMBER && right.getType() == FormulaVariableType.STRING) {
			leftNumberRightString(left, right, result);
		} else if (left.getType() == FormulaVariableType.STRING && right.getType() == FormulaVariableType.NUMBER) {
			leftStringRightNumber(left, right, result);
		} else if (left.getType() == FormulaVariableType.NUMBER && right.getType() == FormulaVariableType.ARRAY) {
			leftNumberRightArray(left, right, result);
		} else if (left.getType() == FormulaVariableType.ARRAY && right.getType() == FormulaVariableType.NUMBER) {
			leftArrayRightNumber(left, right, result);
		} else {
			throw new RuntimeException("Niewspierany typ dodawania: left: " + left + " right: " + right);
		}
		return result;
	}

	private void leftArrayRightNumber(FormulaVariable arrayVar, FormulaVariable numberVar, FormulaVariable result) {
		int value = numberVar.getValueLikeNumber().getInt();
		FormulaArray array = arrayVar.getValueLikeArray();
		
		int start = array.getSize() - value;
		int end = array.getSize();
		
		List<FormulaVariable> view = array.getElements().subList(start,  end);
		
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
		int value = right.getValueLikeNumber().getInt();
		String text = left.getValue().toString().substring((left.getValue().toString().length()) - value);
		result.setFullValue(text);
	}

	private void leftNumberRightArray(FormulaVariable numberVar, FormulaVariable arrayVar, FormulaVariable result) {
		int value = numberVar.getValueLikeNumber().getInt();
		FormulaArray array = arrayVar.getValueLikeArray();
		List<FormulaVariable> view = array.getElements().subList(0, value);
		
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
		int value = left.getValueLikeNumber().getInt();
		String text = right.getValue().toString().substring(0, value);
		result.setFullValue(text);
	}

	private void addString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
//		result.setType(FormulaVariableType.STRING);
		result.setFullValue(((String) left.getValue()) + ((String) right.getValue()));
	}

	private void addNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
//		result.setType(FormulaVariableType.NUMBER);
		FormulaNumber leftNumber = (FormulaNumber) left.getValue();
		result.setFullValue(leftNumber.add((FormulaNumber) right.getValue()));
	}
}

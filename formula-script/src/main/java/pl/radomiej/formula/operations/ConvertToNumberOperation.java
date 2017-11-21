package pl.radomiej.formula.operations;

import org.apache.commons.lang3.math.NumberUtils;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum ConvertToNumberOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right){
		if(right.getType() == FormulaVariableType.NUMBER) return right.copy();
		
		FormulaVariable result = new FormulaVariable();
		result.setType(FormulaVariableType.NUMBER);
		String rawNumberValue = right.getValue().toString();
		if(!NumberUtils.isParsable(rawNumberValue)){
			return FormulaVariable.getNegative();
		}
		
		FormulaNumber number = new FormulaNumber(rawNumberValue);
		result.setValue(number);
		return result;
	}
}

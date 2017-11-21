package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum NotEqualsOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right){
		FormulaVariable result = new FormulaVariable();
		result.setFullValue(1);
		
		if(left.getValue().equals(right.getValue())){
			result.setFullValue(0);
		}
		
		return result;
	}
}

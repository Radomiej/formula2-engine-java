package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum EqualsOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right){
		FormulaVariable result = new FormulaVariable();
		result.setFullValue(0);
		
		if(left.getValue().equals(right.getValue())){
			result.setFullValue(1);
		}
		
		return result;
	}
}

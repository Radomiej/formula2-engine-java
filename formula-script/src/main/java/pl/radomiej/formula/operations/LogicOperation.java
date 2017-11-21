package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.variables.FormulaNumber;

public enum LogicOperation implements FormulaOperation{
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right){
		FormulaVariable result = null;
		
		if (getLogicValue(right) > 0) {
			result = new FormulaVariable(1);
			return result;
		}	
		result = new FormulaVariable(0);
		return result;
	}

	public static int getLogicValue(FormulaVariable variable) {
		if (variable.getValue() instanceof String && !((String) variable.getValue()).isEmpty()) {
			return 1;
		}	
		if (variable.getValue() instanceof FormulaNumber && ((FormulaNumber) variable.getValue()).getBigDecimalValue().signum() > 0) {
			return 1;
		}	
		return 0;
	}
}

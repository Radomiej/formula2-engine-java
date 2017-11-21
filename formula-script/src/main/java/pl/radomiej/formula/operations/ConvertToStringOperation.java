package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum ConvertToStringOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		result.setType(FormulaVariableType.STRING);
		if (left.getValue() != null){
			result.setValue(left.getValue().toString());
		}
		else {
			result.setValue("UNDEFINED");
		}
		return result;
	}
}

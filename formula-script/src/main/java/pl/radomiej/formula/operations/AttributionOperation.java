package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;

public enum AttributionOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right){
		left.setFull(right);
		return left;
	}
}

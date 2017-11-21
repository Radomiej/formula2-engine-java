package pl.radomiej.formula.operations;

import org.apache.commons.lang3.StringUtils;

import pl.radomiej.formula.FormulaComparableHelper;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum EqualsMoreOperation implements FormulaOperation {
	INSTANCE;

	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {

		if (left.getType() != right.getType()) {
			throw new UnsupportedOperationException(
					"Operacja porównania nie jest dozwolona dla ró¿nych typów: " + left + ", " + right);
		}

		int comparator = FormulaComparableHelper.INSTANCE.compareFormulaVariables(left, right);
		FormulaVariable result = new FormulaVariable();

		if(comparator >= 0){
			result.setFullValue(1);
		}else{
			result.setFullValue(0);
		}
		
		return result;
	}
}

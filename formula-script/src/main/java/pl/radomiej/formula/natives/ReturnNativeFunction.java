package pl.radomiej.formula.natives;

import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;

public class ReturnNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		FormulaVariable returnValue = null;
		if (args.size() > 0) {
			returnValue = args.get(0);
		}
		if (returnValue == null) {
			returnValue = new FormulaVariable();
			returnValue.setType(FormulaVariableType.VOID);
		}

		FormulaVariable returnObject = new FormulaVariable();
		returnObject.setType(FormulaVariableType.RETURN);
		returnObject.setValue(returnValue);

		return returnObject;
	}

}
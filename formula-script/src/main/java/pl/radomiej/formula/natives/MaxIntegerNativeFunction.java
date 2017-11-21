package pl.radomiej.formula.natives;

import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

public class MaxIntegerNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		return new FormulaVariable(Integer.MAX_VALUE);
	}
}
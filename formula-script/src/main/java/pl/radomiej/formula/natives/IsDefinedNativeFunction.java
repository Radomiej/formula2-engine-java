package pl.radomiej.formula.natives;

import java.io.PrintStream;
import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;

public class IsDefinedNativeFunction implements NativeFormulaMethod {
	
	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		FormulaVariable one = args.get(0);
		if(one.getType() != FormulaVariableType.UNDEFINED) return FormulaVariable.getTrue();
		return FormulaVariable.getFalse();
	}

}

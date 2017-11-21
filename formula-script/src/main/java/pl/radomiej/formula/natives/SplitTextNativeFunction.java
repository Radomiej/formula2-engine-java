package pl.radomiej.formula.natives;

import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;

public class SplitTextNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		String text = args.get(0).getValue().toString();
		String delimeter = args.get(1).getValue().toString();
		
		FormulaArray resultArray = new FormulaArray();
		String[] results = text.split(delimeter);
		for(String result : results){
			FormulaVariable var = new FormulaVariable(result);
			resultArray.addVariable(var);
		}
		return new FormulaVariable(resultArray);
	}

}
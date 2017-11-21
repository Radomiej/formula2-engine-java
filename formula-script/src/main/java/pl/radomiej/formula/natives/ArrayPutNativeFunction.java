package pl.radomiej.formula.natives;

import java.io.PrintStream;
import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;

public class ArrayPutNativeFunction implements NativeFormulaMethod {
	
	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		FormulaVariable one = args.get(0);
		if(one.getValue() instanceof FormulaArray){
			FormulaArray array = (FormulaArray) one.getValue();
			for(int i = 1; i < args.size(); i++){
				array.addVariable(args.get(i).copy());
			}
			return new FormulaVariable(array.getSize()); 
		}else{
			return new FormulaVariable(0);
		}
	}


}

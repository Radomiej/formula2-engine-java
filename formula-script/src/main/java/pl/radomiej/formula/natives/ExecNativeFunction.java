package pl.radomiej.formula.natives;

import java.util.ArrayList;
import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;

public class ExecNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		FormulaVariable methodName = args.get(0);
		FormulaVariable fileName = args.get(1);
		
		String invokeMethod = methodName.getValue() + "@" + fileName.getValue();
		
		List<FormulaVariable> argsInvokeFunction = new ArrayList<FormulaVariable>();
		for(int i = 2; i < args.size(); i++){
			FormulaVariable nextArg = args.get(i);
			if(nextArg.getType() == FormulaVariableType.NUMBER || nextArg.getType() == FormulaVariableType.STRING){
				nextArg = nextArg.copy();
			}
			argsInvokeFunction.add(nextArg);
		}
		FormulaVariable result = engine.invokeFunction(invokeMethod, argsInvokeFunction);
		
		return result;
	}

}
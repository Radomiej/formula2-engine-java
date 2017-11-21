package pl.radomiej.formula.natives;

import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

public class ReflectionRemoveFieldNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		if (args.get(0).getType() == FormulaVariableType.OBJECT) {
			String removeField = args.get(1).getValueLikeString();
			args.get(0).getValueLikeObject().getFields().remove(removeField);
		}else if (args.get(0).getType() == FormulaVariableType.ARRAY) {
			int removeIndex = args.get(1).getValueLikeNumber().getInt();
			args.get(0).getValueLikeArray().removeAt(removeIndex);
		}else{
			return FormulaVariable.getFalse();
		}

		return FormulaVariable.getTrue();
		// throw new UnsupportedOperationException("Niewspierana forma tworzenia
		// obiektu");
	}

	private FormulaVariable createObject(List<FormulaVariable> args) {
		FormulaVariable objectVariable = new FormulaVariable();
		FormulaObject object = new FormulaObject();
		objectVariable.setFullValue(object);

		for (FormulaVariable arg : args) {
			String fieldName = (String) arg.getValue();
			object.addField(fieldName);
		}

		return objectVariable;
	}

	private FormulaVariable createArray(FormulaVariable one) {
		FormulaVariable arrayVariable = new FormulaVariable();
		FormulaArray array = new FormulaArray();
		arrayVariable.setFullValue(array);

		int size = ((FormulaNumber) one.getValue()).getInt();
		for (int i = 0; i < size; i++) {
			array.addVariable(FormulaVariable.getUndefined());
		}
		return arrayVariable;
	}

}
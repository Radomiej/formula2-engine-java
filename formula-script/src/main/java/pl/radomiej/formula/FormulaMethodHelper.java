package pl.radomiej.formula;

import java.util.List;

public class FormulaMethodHelper {

	@SuppressWarnings("unchecked")
	public static <T> T getOptionalArgument(List<FormulaVariable> args, int index, T defaultValue) {
		if(args.size() <= index) return defaultValue;
		FormulaVariable formulaVariable = args.get(index);
		if(defaultValue instanceof Integer){
			return (T) formulaVariable.getValueLikeNumber().getIntObj();
		}else if(defaultValue instanceof Float){
			return (T) formulaVariable.getValueLikeNumber().getFloatObj();
		}else if(defaultValue instanceof Double){
			return (T) formulaVariable.getValueLikeNumber().getDoubleObj();
		}else if(defaultValue instanceof Long){
			return (T) formulaVariable.getValueLikeNumber().getLongObj();
		}else if(defaultValue instanceof String){
			return (T) formulaVariable.getValueLikeString();
		}
		
		System.err.println("Niewspierany typ opcjonalnego argumentu: " + defaultValue.getClass());
		return defaultValue;
	}

	public static FormulaVariable getOptionalArgumentOrNull(List<FormulaVariable> args, int index) {
		if(args.size() <= index) return null;
		FormulaVariable formulaVariable = args.get(index);
		return formulaVariable;
	}

}

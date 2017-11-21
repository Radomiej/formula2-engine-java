package pl.radomiej.formula.operations;

import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;

public enum InvertOperation implements FormulaOperation{
	INSTANCE;
	
	public FormulaVariable invoke(FormulaVariable left, FormulaVariable right) {
		FormulaVariable result = new FormulaVariable();
		if (left.getType() == right.getType() && left.getType() == FormulaVariableType.NUMBER) {
			invertNumber(left, right, result);
		} else if (left.getType() == right.getType() && left.getType() == FormulaVariableType.STRING) {
			invertString(left, right, result);
		} else {
			throw new RuntimeException("Niewspierany typ odwrócenia: left: " + left + " right: " + right);
		}
		return result;
	}

	private void invertString(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		char[] word = left.getValueLikeString().toCharArray();
		for(int i = 0; i < word.length; i++){
			char current = word[i];
			if(Character.isUpperCase(current)){
				word[i] = Character.toUpperCase(current);
			}else{
				word[i] = Character.toLowerCase(current);
			}
		}
		
		result.setFullValue(new String(word));
	}

	private void invertNumber(FormulaVariable left, FormulaVariable right, FormulaVariable result) {
		boolean value = result.getValueLikeBoolean();
		result.setFullValue(!value);
	}
	
}

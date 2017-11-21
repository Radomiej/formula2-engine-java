package pl.radomiej.formula.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pl.radomiej.formula.FormulaInstruction;
import pl.radomiej.formula.FormulaLexyconHelper;
import pl.radomiej.formula.FormulaLine;
import pl.radomiej.formula.FormulaMacro;
import pl.radomiej.formula.macro.LogicMacro;

public class LogicMacroParser {

	private LogicMacro result;
	private int nested = 1;
	
	public LogicMacroParser(StringBuilder rawConditions) {
//		rawConditions = new StringBuilder(StringUtils.deleteWhitespace(rawConditions.toString()));
		rawConditions = new StringBuilder(rawConditions.toString());
		result = new LogicMacro();
		
		List<String> rawLinesCondition = new ArrayList<String>();
		readSegment(rawConditions, rawLinesCondition);
		result.getCondition().addAll(FormulaLexyconHelper.getInstructionsFromRaws(rawLinesCondition));
		
		
		List<String> rawLinesPositive = new ArrayList<String>();
		readSegment(rawConditions, rawLinesPositive);
		result.getInstructions().addAll(FormulaLexyconHelper.getInstructionsFromRaws(rawLinesPositive));
		
		
		if(rawConditions.charAt(0) == '|' && rawConditions.charAt(1) == '|'){
			//Simple parse negative position
			List<String> rawLinesNegative = new ArrayList<String>();
			readSegment(rawConditions, rawLinesNegative);
			result.getNegative().addAll(FormulaLexyconHelper.getInstructionsFromRaws(rawLinesNegative));
		}else if(rawConditions.substring(0, 1).equals("|?")){// else if support
			rawConditions.setCharAt(0, '{');
			LogicMacro negativeMacro = new LogicMacroParser(rawConditions).getResult();
			result.getNegative().add(negativeMacro);
		}
	}

	private void readSegment(StringBuilder rawConditions, List<String> rawLinesCondition) {
		StringBuilder currentLine = new StringBuilder();
		for (int x = 2; x < rawConditions.length(); x++) {
			if (rawConditions.charAt(x - 1) == '{' && (rawConditions.charAt(x) == '?' || rawConditions.charAt(x) == '!')) {
				nested++;
			}
			if (rawConditions.charAt(x) == '}' && (rawConditions.charAt(x - 1) == '?' || rawConditions.charAt(x - 1) == '!')) {
				nested--;
			}
			if (nested == 1) {
				if(rawConditions.charAt(x) == ';'){
					rawLinesCondition.add(currentLine.toString());
					currentLine = new StringBuilder();
					continue;
				}else if(rawConditions.charAt(x) == '|'){
					rawLinesCondition.add(currentLine.toString());
					rawConditions.delete(0, x);
					break;
				}
			}
			currentLine.append(rawConditions.charAt(x));
		}
		if(nested == 0){
			currentLine.deleteCharAt(currentLine.length()-1);
			currentLine.deleteCharAt(currentLine.length()-1);
			rawLinesCondition.add(currentLine.toString());
		}
	}


	public LogicMacro getResult() {
		return result;
	}
}

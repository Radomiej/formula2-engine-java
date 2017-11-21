package pl.radomiej.formula.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pl.radomiej.formula.FormulaInstruction;
import pl.radomiej.formula.FormulaLexyconHelper;
import pl.radomiej.formula.FormulaLine;
import pl.radomiej.formula.macro.IterationMacro;
import pl.radomiej.formula.macro.LogicMacro;

public class IterationMacroParser {
	private IterationMacro result;
	private int nested = 1;

	public IterationMacroParser(StringBuilder rawConditions) {
		// rawConditions = new
		// StringBuilder(StringUtils.deleteWhitespace(rawConditions.toString()));
		result = new IterationMacro();

		// Special logic for loop counter initializer if exist
		readLoopCounter(rawConditions);

		//Zawiera sekcje condition
		if (FormulaLexyconHelper.containsInContext("|?", rawConditions)) {
			List<String> rawLinesCondition = new ArrayList<String>();
			readSegment(rawConditions, rawLinesCondition);
			result.getCondition().addAll(FormulaLexyconHelper.getInstructionsFromRaws(rawLinesCondition));
		}

		//Zawiera sekcje statements
		if (FormulaLexyconHelper.containsInContext("|!", rawConditions)) {
			List<String> rawLinesPositive = new ArrayList<String>();
			readSegment(rawConditions, rawLinesPositive);
			result.getInstructions().addAll(FormulaLexyconHelper.getInstructionsFromRaws(rawLinesPositive));
		}

		if(result.getInitLoop().isEmpty() && result.getCondition().isEmpty() && result.getInstructions().isEmpty()){
			throw new UnsupportedOperationException("Nie poprawny zapis instrukcji iteracyjnej {! !}");
		}
	}

	private void readLoopCounter(StringBuilder rawConditions) {
		StringBuilder currentLine = new StringBuilder();
		for (int x = 2; x < rawConditions.length(); x++) {
			if (rawConditions.charAt(x - 1) == '{'
					&& (rawConditions.charAt(x) == '?' || rawConditions.charAt(x) == '!')) {
				nested++;
			}
			if (rawConditions.charAt(x) == '}'
					&& (rawConditions.charAt(x - 1) == '?' || rawConditions.charAt(x - 1) == '!')) {
				nested--;
			}
			if (nested == 1) {
				if (rawConditions.charAt(x) == '|') {
					FormulaLine initLine = new FormulaLine(currentLine.toString());
					result.getInitLoop().add(initLine);
					rawConditions.delete(0, x);
					break;
				}
			}
			currentLine.append(rawConditions.charAt(x));
		}
	}

	private void readSegment(StringBuilder rawConditions, List<String> rawLinesCondition) {
		StringBuilder currentLine = new StringBuilder();
		for (int x = 2; x < rawConditions.length(); x++) {
			if (rawConditions.charAt(x - 1) == '{'
					&& (rawConditions.charAt(x) == '?' || rawConditions.charAt(x) == '!')) {
				nested++;
			}
			if (rawConditions.charAt(x) == '}'
					&& (rawConditions.charAt(x - 1) == '?' || rawConditions.charAt(x - 1) == '!')) {
				nested--;
			}
			if (nested == 1) {
				if (rawConditions.charAt(x) == ';') {
					rawLinesCondition.add(currentLine.toString());
					currentLine = new StringBuilder();
					continue;
				} else if (rawConditions.charAt(x) == '|') {
					rawLinesCondition.add(currentLine.toString());
					rawConditions.delete(0, x);
					break;
				}
			}
			currentLine.append(rawConditions.charAt(x));
		}
		if (nested == 0) {
			currentLine.deleteCharAt(currentLine.length() - 1);
			currentLine.deleteCharAt(currentLine.length() - 1);
			rawLinesCondition.add(currentLine.toString());
		}
	}

	public IterationMacro getResult() {
		return result;
	}
}

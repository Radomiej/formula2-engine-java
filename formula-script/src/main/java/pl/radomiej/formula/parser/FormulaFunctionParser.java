package pl.radomiej.formula.parser;

import pl.radomiej.formula.FormulaFunction;
import pl.radomiej.formula.FormulaLexyconHelper;
import pl.radomiej.formula.FormulaLine;
import pl.radomiej.formula.FormulaMacro;

public class FormulaFunctionParser {

	private int i = 0;
	private FormulaFunction parseFunction;
	
	public FormulaFunctionParser(String functionScript) {
		this(functionScript, false);
	}
	
	public FormulaFunctionParser(String functionScript, boolean simpleAnoymousfunction) {
		if(simpleAnoymousfunction) functionScript = prepareToParseFromSimpleAnoymousQuery(functionScript);
		String[] rawLines = functionScript.split(System.lineSeparator());
		parseFunction(rawLines);
	}
	
	private String prepareToParseFromSimpleAnoymousQuery(String functionScript) {
		if(functionScript.charAt(functionScript.length() - 1) == ')'){
			functionScript = functionScript.substring(0, functionScript.length() - 1);
			functionScript = functionScript.substring(0 ,functionScript.lastIndexOf('('));
		}
		if(functionScript.charAt(0) == '"' && functionScript.charAt(functionScript.length()-1) == '"'){
			functionScript = functionScript.substring(1, functionScript.length() - 1);
		}
		return functionScript;
	}

	/**
	 * 
	 * @param rawLines
	 *            tablica z linią kodu
	 * @param i
	 *            aktualna linia kodu
	 * @return ostatnia przetworzona linia kodu
	 */
	private void parseFunction(String[] rawLines) {
		parseFunction = new FormulaFunction();

		for (; i < rawLines.length; i++) {
			if (FormulaLexyconHelper.isEmptyLine(rawLines[i])) {
				continue;
			} else if (FormulaLexyconHelper.isFunctionName(rawLines[i])) {
				i--;
				return;
			} else if (FormulaLexyconHelper.isSpecialMacro(rawLines[i])) {
				FormulaMacro macro = parseSpecialMacro(rawLines);
				parseFunction.addMacro(macro);
			} else {
				FormulaMacro macro = parseDefaultMacro(rawLines);
				parseFunction.addMacro(macro);
			}
		}
	}

	/**
	 * 
	 * @param rawLines
	 *            tablica z lini� kodu
	 * @param i
	 *            aktualna linia kodu
	 * @return
	 */
	private FormulaMacro parseDefaultMacro(String[] rawLines) {
		FormulaMacro result = new FormulaMacro();

		String rawLine;
		i--;
		do {
			i++;
			rawLine = rawLines[i];
			// rawLine = StringUtils.deleteWhitespace(rawLines[i]);
			if (FormulaLexyconHelper.isEmptyLine(rawLine)) {
				continue;
			}
			if (FormulaLexyconHelper.isSpecialMacro(rawLine)) {
				i--;
				break;
			}

			FormulaLine line = new FormulaLine(rawLine);
			result.addInstruction(line);
		} while (isNextInstruction(rawLine, rawLines));
		return result;
	}

	//TODO wsparcie dla elastycznego parsowania w kt�rym nie potrzeba sprawdzania �rednik�w �eby zobaczy� ostatni� lini� instrukcji
	private boolean isNextInstruction(String rawLine, String[] rawLines) {
		if(i >= rawLines.length - 1) return false;
		return rawLine.contains(";");
	}

	private FormulaMacro parseSpecialMacro(String[] rawLines) {
		String startLine = rawLines[i].trim();
		if (startLine.startsWith("{?")) {
			return new LogicMacroParser(getMainMacroRaw(rawLines)).getResult();
		} else if (startLine.startsWith("{!")) {
			return new IterationMacroParser(getMainMacroRaw(rawLines)).getResult();
		}

		throw new RuntimeException("Specjialne Macro: " + startLine.substring(0, 2) + " nie jest jeszce wspierane");
	}

	private StringBuilder getMainMacroRaw(String[] rawLines) {
		StringBuilder rawConditions = new StringBuilder();

		int nested = 0;
		for (; i < rawLines.length; i++) {
			String startLine = rawLines[i].trim();
			rawConditions.append(startLine.charAt(0));
			for (int x = 1; x < startLine.length(); x++) {
				if (startLine.charAt(x - 1) == '{' && (startLine.charAt(x) == '?' || startLine.charAt(x) == '!')) {
					nested++;
				}
				if (startLine.charAt(x) == '}' && (startLine.charAt(x - 1) == '?' || startLine.charAt(x - 1) == '!')) {
					nested--;
				}
				if (nested == 0) {
					rawConditions.append("}");
					i++;
					return rawConditions;
				}
				rawConditions.append(startLine.charAt(x));
			}
		}
		throw new RuntimeException("B��d zagnie�dzenia macro");
	}

	public FormulaFunction getFunction() {
		return parseFunction.copy();
	}
}

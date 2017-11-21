package pl.radomiej.formula.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaFunction;
import pl.radomiej.formula.FormulaInstruction;
import pl.radomiej.formula.FormulaLexyconHelper;
import pl.radomiej.formula.FormulaLine;
import pl.radomiej.formula.FormulaMacro;
import pl.radomiej.formula.macro.LogicMacro;

public class FormulaParser {

	private FormulaEngine engine;
	private int i = 1;
	private String fileName;

	public FormulaParser(String rawScript, FormulaEngine engine, String fileName) {
		this.engine = engine;
		this.fileName = fileName;
		
		String[] rawLines = rawScript.split(System.lineSeparator());
		for (; i < rawLines.length; i++) {
			if (FormulaLexyconHelper.isEmptyLine(rawLines[i])) {
				continue;
			} else if (FormulaLexyconHelper.isFunctionName(rawLines[i])) {
				parseFunction(rawLines);
			}
		}
	}

	public FormulaParser(String rawScript, FormulaEngine engine) {
		this(rawScript, engine, "");
	}

	/**
	 * 
	 * @param rawLines
	 *            tablica z lini� kodu
	 * @param i
	 *            aktualna linia kodu
	 * @return ostatnia przetworzona linia kodu
	 */
	private void parseFunction(String[] rawLines) {
		FormulaFunction function = new FormulaFunction();
		String functionName = FormulaLexyconHelper.getFunctionName(rawLines[i], fileName);
		engine.addFunction(functionName, function);
		function.setName(functionName);
		i++;

		for (; i < rawLines.length; i++) {
			if (FormulaLexyconHelper.isEmptyLine(rawLines[i])) {
				continue;
			} else if (FormulaLexyconHelper.isFunctionName(rawLines[i])) {
				i--;
				return;
			} else if (FormulaLexyconHelper.isSpecialMacro(rawLines[i])) {
				FormulaMacro macro = parseSpecialMacro(rawLines);
				function.addMacro(macro);
			} else {
				FormulaMacro macro = parseDefaultMacro(rawLines);
				function.addMacro(macro);
			}
		}
	}

	/**
	 * 
	 * @param rawLines
	 *            tablica z linią kodu
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
			if(i >= rawLines.length) break; //Zabezpieczenie przed �rednikiem na ko�cu pliku w FORMULI
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
		} while (isLastInstruction(rawLine));
		return result;
	}

	private boolean isLastInstruction(String rawLine) {
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
			if(startLine.length() == 0) continue;
			
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
//					i++;
					return rawConditions;
				}
				rawConditions.append(startLine.charAt(x));
			}
		}
		throw new RuntimeException("B��d zagnie�dzenia macro");
	}

}

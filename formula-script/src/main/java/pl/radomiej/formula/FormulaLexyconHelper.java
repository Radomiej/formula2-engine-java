package pl.radomiej.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import pl.radomiej.formula.parser.IterationMacroParser;
import pl.radomiej.formula.parser.LogicMacroParser;

//TODO Doda� UnitTesty
public class FormulaLexyconHelper {
	/**
	 * Sprawdza czy jest to funkcja natywna lub wywo�anie funkcji anonimowej z
	 * zmiennej
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isFunction(String query) {
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (!Character.isLetter(ch) && ch != '_') {
				if (ch != '(')
					return false;
				if (query.charAt(query.length() - 1) == ')')
					return true;
				return false;
			}
		}
		return true;
	}

	/**
	 * Sprawdza czy jest to proste wywo�anie funkcji anonimowej bezpo�rednio z
	 * cudzys�owia.
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isSimpleAnoymousFunction(String query) {
		query = removeLastSemicolonIfExistAndTrim(query);
		if (query.charAt(0) == '"'
				&& (query.charAt(query.length() - 1) == ')' || (query.charAt(query.length() - 1) == '"'))) {
			// TODO kompleksowe sprawdzenie czy jest to prosta funkcja anonimowa
			return true;
		}
		return false;
	}

	public static boolean isConstans(String query) {
		query = removeLastSemicolonIfExistAndTrim(query);

		if (NumberUtils.isCreatable(query))
			return true;
		if (query.length() >= 2 && query.charAt(0) == '\'' && query.charAt(query.length() - 1) == '\'')
			return true;

		return false;
	}

	public static boolean containsBracketsOnStartAndEndQuery(String query) {
		query = removeLastSemicolonIfExistAndTrim(query);
		if (query.length() > 0 && query.charAt(0) == '(' && query.charAt(query.length() - 1) == ')')
			return true;
		return false;
	}

	public static boolean isConstansString(String query) {
		query = removeLastSemicolonIfExistAndTrim(query);
		if (query.charAt(0) == '\'' && query.charAt(query.length() - 1) == '\'')
			return true;
		return false;
	}

	private static String removeLastSemicolonIfExistAndTrim(String query) {
		query = query.trim();
		if (query.length() < 1)
			return query;

		if (query.charAt(query.length() - 1) == ';') {
			return query.substring(0, query.length() - 1);
		}
		return query;
	}

	public static String getStringValueFromQuery(String query) {
		query = removeLastSemicolonIfExistAndTrim(query);
		StringBuilder stringBuilder = new StringBuilder(query);
		stringBuilder.deleteCharAt(0);
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}

	public static boolean containsBrackets(String query) {

		StringBuilder queryBuffer = new StringBuilder(query);
		int openCrecketIndex = findFirstSpecialWithBrecketIgnoring("(", queryBuffer);
		return openCrecketIndex >= 0 && findFirstSpecialWithBrecketIgnoring(")", queryBuffer) > openCrecketIndex;
		// return query.contains("(") && query.contains(")");
	}

	public static boolean isEmptyLine(String rawLine) {
		return rawLine.trim().equals("") || rawLine.trim().startsWith(":");
	}

	public static boolean isSpecialMacro(String rawLine) {
		rawLine = rawLine.trim();
		return rawLine.startsWith("{?") || rawLine.startsWith("{!");
	}

	public static boolean isFunctionName(String rawLine) {
		if (rawLine.startsWith("\\") && Character.isLetter(rawLine.charAt(1)))
			return true;
		return false;
	}

	public static String getFunctionName(String rawLine, String fileName) {
		rawLine = rawLine.trim();
		rawLine = rawLine.replace("\\", "");
		rawLine += "@";
		if (fileName != null)
			rawLine += fileName;
		return rawLine;
	}

	public static String prepareQuery(String query) {
		return removeLastSemicolonIfExistAndTrim(query);
	}

	public static boolean containsInContext(String searchSequence, StringBuilder query) {
		return findFirstSpecialWithBrecketIgnoring(searchSequence, query) >= 0;
	}

	public static int findLastSpecialWithBrecketIgnoring(String specialChar, StringBuilder buffor) {
		return findLastSpecialWithBrecketIgnoring(specialChar, buffor, buffor.length() - 1);
	}

	public static int findLastSpecialWithBrecketIgnoring(String specialChar, StringBuilder buffor, int endIndex) {
		List<Integer> brecketsRange = getBrecketRange(buffor.toString());
		List<Integer> squareBrecketsRange = getSquareBrecketRange(buffor.toString());
		List<Integer> apostrofRange = getApostrofRange(buffor.toString());
		List<Integer> quotationRange = getQuotationRange(buffor.toString());

		int end = endIndex;
		while (true) {
			int k = buffor.lastIndexOf(specialChar, end);
			if (k == -1)
				break;

			// Je�li invalid true - dany k nienale�y do szukanego contextu.
			boolean invalid = false;

			// Sprawdza czy k znajduje si� poza nawiasem
			for (int i = 0; i < brecketsRange.size(); i += 2) {
				int sIndex = brecketsRange.get(i) + 1;
				int eIndex = brecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					end = k - 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza nawiasami kwadratowymi
			for (int i = 0; i < squareBrecketsRange.size(); i += 2) {
				int sIndex = squareBrecketsRange.get(i) + 1;
				int eIndex = squareBrecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					end = k - 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza apostrofami
			for (int i = 0; i < apostrofRange.size(); i += 2) {
				int sIndex = apostrofRange.get(i) + 1;
				int eIndex = apostrofRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					end = k - 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza cudzys�owiami
			for (int i = 0; i < quotationRange.size(); i += 2) {
				int sIndex = quotationRange.get(i) + 1;
				int eIndex = quotationRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					end = k - 1;
					invalid = true;
					break;
				}
			}

			if (!invalid) {
				return k;
			}
		}

		return -1;
	}

	private static int findFirstSpecialWithBrecketIgnoring(String specialChar, StringBuilder buffor) {
		List<Integer> brecketsRange = getBrecketRange(buffor.toString());
		List<Integer> squareBrecketsRange = getSquareBrecketRange(buffor.toString());
		List<Integer> apostrofRange = getApostrofRange(buffor.toString());
		List<Integer> quotationRange = getQuotationRange(buffor.toString());

		int start = 0;
		while (true) {
			int k = buffor.indexOf(specialChar, start);
			if (k == -1)
				break;

			// Je�li invalid true - dany k nienale�y do szukanego contextu.
			boolean invalid = false;

			// Sprawdza czy k znajduje si� poza nawiasem
			for (int i = 0; i < brecketsRange.size(); i += 2) {
				int sIndex = brecketsRange.get(i) + 1;
				int eIndex = brecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza nawiasem
			for (int i = 0; i < squareBrecketsRange.size(); i += 2) {
				int sIndex = squareBrecketsRange.get(i) + 1;
				int eIndex = squareBrecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza apostrofami
			for (int i = 0; i < apostrofRange.size(); i += 2) {
				int sIndex = apostrofRange.get(i) + 1;
				int eIndex = apostrofRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje si� poza cudzys�owiami
			for (int i = 0; i < quotationRange.size(); i += 2) {
				int sIndex = quotationRange.get(i) + 1;
				int eIndex = quotationRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			if (!invalid) {
				return k;
			}
		}

		return -1;
	}

	private static List<Integer> getSquareBrecketRange(String query) {
		List<Integer> brecketsRange = new LinkedList<Integer>();
		int nested = 0;
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '[') {
				if (nested == 0)
					brecketsRange.add(i);
				nested++;
			} else if (ch == ']') {
				if (nested == 1)
					brecketsRange.add(i);
				nested--;
			}

		}

		if (nested > 0) {
			throw new RuntimeException("Za dużo nawiasów kwadratowych w wyrażeniu: " + query);
		}
		return brecketsRange;
	}

	private static List<Integer> getBrecketRange(String query) {
		List<Integer> brecketsRange = new LinkedList<Integer>();
		int nested = 0;
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '(') {
				if (nested == 0)
					brecketsRange.add(i);
				nested++;
			} else if (ch == ')') {
				if (nested == 1)
					brecketsRange.add(i);
				nested--;
			}

		}

		if (nested > 0) {
			throw new FormulaRuntimeException("Za dużo nawiasów w wyrażeniu: " + query);
		}
		return brecketsRange;
	}

	private static List<Integer> getBuckleBrecketRange(String query) {
		List<Integer> brecketsRange = new LinkedList<Integer>();
		int nested = 0;
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '{') {
				if (nested == 0)
					brecketsRange.add(i);
				nested++;
			} else if (ch == '}') {
				if (nested == 1)
					brecketsRange.add(i);
				nested--;
			}

		}

		if (nested > 0) {
			throw new RuntimeException("Za dużo nawiasów w wyrażeniu: " + query);
		}
		return brecketsRange;
	}

	// TODO dodać wsparcie dla natywnych apostrofów wpisywanych w tekst formuli
	private static List<Integer> getApostrofRange(String query) {
		List<Integer> apostrofsRange = new LinkedList<Integer>();
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '\'') {
				apostrofsRange.add(i);
			}
		}

		if (apostrofsRange.size() % 2 == 1) {
			throw new RuntimeException("Błędna liczba apostrofów w wyrazeniu: " + query);
		}
		return apostrofsRange;
	}

	private static List<Integer> getQuotationRange(String query) {
		List<Integer> apostrofsRange = new LinkedList<Integer>();
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '\"') {
				apostrofsRange.add(i);
			}
		}

		if (apostrofsRange.size() % 2 == 1) {
			throw new RuntimeException("Błędna liczba cudzysłowiów w wyrażeniu: " + query);
		}
		return apostrofsRange;
	}

	// TODO dzielenie z uwzględnieniem kontekstu
	public static String[] split(String query, String splitter) {
		StringBuilder buffor = new StringBuilder(query);
		List<Integer> splitIndexes = findAllSpecialWithBrecketIgnoring(splitter, buffor);
		if (splitIndexes.isEmpty())
			return new String[] { query };

		String[] frags = new String[splitIndexes.size() + 1];
		int prevIndex = 0;
		for (int i = 0; i < splitIndexes.size(); i++) {
			int currentIndex = splitIndexes.get(i);
			frags[i] = buffor.substring(prevIndex, currentIndex);
			prevIndex = currentIndex + splitter.length();
		}
		frags[splitIndexes.size()] = buffor.substring(prevIndex);

		return frags;
	}

	private static List<Integer> findAllSpecialWithBrecketIgnoring(String specialChar, StringBuilder buffor) {
		List<Integer> brecketsRange = getBrecketRange(buffor.toString());
		List<Integer> squareBrecketsRange = getSquareBrecketRange(buffor.toString());
		List<Integer> buckleBrecketsRange = getBuckleBrecketRange(buffor.toString());
		List<Integer> apostrofRange = getApostrofRange(buffor.toString());
		List<Integer> quotationRange = getQuotationRange(buffor.toString());

		List<Integer> specialCharOccurences = new ArrayList<Integer>();
		int start = 0;
		while (true) {
			int k = buffor.indexOf(specialChar, start);
			if (k == -1)
				break;

			// Je�li invalid true - dany k nienale�y do szukanego contextu.
			boolean invalid = false;

			// Sprawdza czy k znajduje się poza nawiasem okrągłym
			for (int i = 0; i < brecketsRange.size(); i += 2) {
				int sIndex = brecketsRange.get(i) + 1;
				int eIndex = brecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje się poza nawiasem kwadratowym
			for (int i = 0; i < squareBrecketsRange.size(); i += 2) {
				int sIndex = squareBrecketsRange.get(i) + 1;
				int eIndex = squareBrecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje się poza nawiasem klamrowym
			for (int i = 0; i < buckleBrecketsRange.size(); i += 2) {
				int sIndex = buckleBrecketsRange.get(i) + 1;
				int eIndex = buckleBrecketsRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje się poza apostrofami
			for (int i = 0; i < apostrofRange.size(); i += 2) {
				int sIndex = apostrofRange.get(i) + 1;
				int eIndex = apostrofRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			// Sprawdza czy k znajduje się poza cudzysłowiami
			for (int i = 0; i < quotationRange.size(); i += 2) {
				int sIndex = quotationRange.get(i) + 1;
				int eIndex = quotationRange.get(i + 1) - 1;
				if (k >= sIndex && k <= eIndex) {
					start = k + 1;
					invalid = true;
					break;
				}
			}

			if (!invalid) {
				specialCharOccurences.add(k);
				start = k + 1;
			}
		}

		return specialCharOccurences;
	}

	/**
	 * Pobiera instrukcję z Macro np. Logic/Iteration i zwraca listę instrukcji
	 * do wykonania
	 * 
	 * @param rawLinesCondition
	 * @return
	 */
	public static Collection<? extends FormulaInstruction> getInstructionsFromRaws(List<String> rawLinesCondition) {
		List<FormulaInstruction> instructions = new LinkedList<FormulaInstruction>();
		for (String rawLine : rawLinesCondition) {
			if (FormulaLexyconHelper.isEmptyLine(rawLine)) {
				continue;
			}
			if (rawLine.startsWith("{?")) {
				FormulaInstruction logicInstruction = new LogicMacroParser(new StringBuilder(rawLine)).getResult();
				instructions.add(logicInstruction);
			} else if (rawLine.startsWith("{!")) {
				FormulaInstruction iterationInstruction = new IterationMacroParser(new StringBuilder(rawLine))
						.getResult();
				instructions.add(iterationInstruction);
			} else {
				FormulaLine line = new FormulaLine(rawLine);
				instructions.add(line);
			}
		}
		return instructions;
	}
}

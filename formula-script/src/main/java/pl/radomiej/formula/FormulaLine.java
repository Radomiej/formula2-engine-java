package pl.radomiej.formula;

import java.util.LinkedList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import pl.radomiej.formula.operations.FormulaOperationType;

public class FormulaLine implements FormulaInstruction {
	private FormulaLeaf operation;

	public FormulaLine(String lineText) {
		lineText = lineText.trim();
		operation = new FormulaLeaf(lineText);

		if (isComplexOperation(operation)) {
			divideOperation(operation);
		} else {
			operation.setSimpleQuery(true);
		}
	}

	private void divideOperation(FormulaLeaf operation2) {
		int[] operationChar = getLastOperationSymbol(operation2);
		if (operationChar == null) {
			System.err.println("Błąd składni: " + operation2.getQuery());
		}

		String rawRight = getRightSide(operationChar[1], operation2);

		String queryRight = "";
		try {
			queryRight = prepareQuery(rawRight);
			FormulaLeaf right = new FormulaLeaf(queryRight);
			operation2.setRight(right);

			if (isComplexOperation(right)) {
				divideOperation(right);
			} else {
				right.setSimpleQuery(true);
			}
		} catch (StringIndexOutOfBoundsException ex) {
			System.err.println("Błąd składni: " + operation2.getQuery());
		}

		// Zabezpieczenie przed pobraniem lewej strony dla operatora
		// jednoargumentowego jak konwersja typ�w $,#
		if (operationChar[0] > 0) {
			String rawLeft = getLeftSide(operationChar[0], operation2);
			String queryLeft = prepareQuery(rawLeft);
			FormulaLeaf left = new FormulaLeaf(queryLeft);
			operation2.setLeft(left);

			if (isComplexOperation(left)) {
				divideOperation(left);
			} else {
				left.setSimpleQuery(true);
			}
		} else {// Je�li lewa strona nie mo�e by� pobrana to kopiuje leaf z
				// wartosci prawej
			FormulaLeaf left = new FormulaLeaf(queryRight);
			operation2.setLeft(left);
			left.setSimpleQuery(true);
		}
	}

	/**
	 * Pobiera lewa czesc operacji od aktualnego indexu lini do przekazanego
	 * indeksu
	 * 
	 * @param end
	 *            indeks konca wycinania
	 * @return lewa cz�� wyra�enia
	 */
	public String getLeftSide(int end, FormulaLeaf operation2) {
		return operation2.getQuery().substring(0, end);
	}

	/**
	 * Pobiera praw� czesc operacji od aktualnego indexu lini do przekazanego
	 * indeksu
	 * 
	 * @param start
	 *            indeks konca wycinania
	 * @return prawa cz�� wyra�enia
	 */
	public String getRightSide(int start, FormulaLeaf operation2) {
		return operation2.getQuery().substring(start);
	}

	private String prepareQuery(String query) {
		query = query.trim();
		StringBuilder queryBuilder = new StringBuilder(query);
		if (query.charAt(0) == '(' && query.charAt(query.length() - 1) == ')') {
			queryBuilder.deleteCharAt(query.length() - 1);
			queryBuilder.deleteCharAt(0);
		}

		// List<Integer> brecketsRange = getBrecketRange(query);
		// if(brecketsRange.isEmpty()) return query;
		//
		// StringBuilder queryBuilder = new StringBuilder(query);
		// for(int i = brecketsRange.size() - 1; i >= 0; i -= 2){
		// int sIndex = brecketsRange.get(i);
		// int eIndex = brecketsRange.get(i-1);
		// queryBuilder.deleteCharAt(sIndex);
		// queryBuilder.deleteCharAt(eIndex);
		// }
		return queryBuilder.toString();
	}

	/**
	 * Sprawdza czy dany leaf jest operacją złożoną, mogącą być rozbitą na
	 * kolejne operacje
	 * 
	 * @param leaf
	 * @return true jeśli leaf jest złożony
	 */
	private boolean isComplexOperation(FormulaLeaf leaf) {
		String query = leaf.getQuery();
		StringBuilder queryBuilder = new StringBuilder(query);

		if (StringUtils.isNumeric(query))
			return false;
		if (NumberUtils.isCreatable(query))
			return false;
		if (FormulaLexyconHelper.containsBracketsOnStartAndEndQuery(query))
			return false;
		if (FormulaLexyconHelper.isFunction(query))
			return false;
		if (FormulaLexyconHelper.isSimpleAnoymousFunction(query))
			return false;

		int k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("+", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("-", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("*", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("/", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("%", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("|", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("&", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("=", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("<", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(">", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("$", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("#", queryBuilder);
		if (k >= 0)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(";", queryBuilder);
		if (k >= 0 && k < queryBuilder.length() - 1)
			return true;
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("~", queryBuilder);
		if (k >= 0 && k < queryBuilder.length() - 1)
			return true;
		// if (leaf.getQuery().contains("+"))
		// return true;
		// if (leaf.getQuery().contains("-"))
		// return true;
		// if (leaf.getQuery().contains("*"))
		// return true;
		// if (leaf.getQuery().contains("/"))
		// return true;
		// if (leaf.getQuery().contains("%"))
		// return true;
		// if (leaf.getQuery().contains("|"))
		// return true;
		// if (leaf.getQuery().contains("="))
		// return true;
		// if (leaf.getQuery().contains("<"))
		// return true;
		// if (leaf.getQuery().contains(">"))
		// return true;
		// if (leaf.getQuery().contains("$"))
		// return true;
		// if (leaf.getQuery().contains("#"))
		// return true;
		// if (leaf.getQuery().contains("&"))
		// return true;
		// if (leaf.getQuery().contains("|"))
		// return true;

		return false;
	}

	private int[] getLastOperationSymbol(FormulaLeaf currentOperation) {
		StringBuilder buffor = new StringBuilder(currentOperation.getQuery());
		// Pierwsza operacja jest wykonywana ostatnia, ma najni�szy priorytet
		// Operacja podzia�u/ wieleinstrukcji w jednej linii
		int k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(";", buffor, buffor.length() - 2);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.INSTRUCTION);
			return result;
		}

		// Operacja przypisania
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(":=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.ATTRIBUTION);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("+=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.ATTRIBUTION_ADD);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("-=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.ATTRIBUTION_SUBTRACTION);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("*=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.ATTRIBUTION_MULTIPLICATION);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("/=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.ATTRIBUTION_DIVIDE);
			return result;
		}

		// OPERATORY LOGICZNE
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("&", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.AND);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("|", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.OR);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("!=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.NOT_EQUALS);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("<=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.EQUALS_LESS);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(">=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 2 };
			currentOperation.setType(FormulaOperationType.EQUALS_MORE);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("=", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.EQUALS);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("<", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.LESS);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring(">", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.MORE);
			return result;
		}

		// OPERATORY ARYTMETYCZNE artmetyczne
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("+", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.ADD);
			return result;
		}
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("-", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.SUBTRACTION);
			return result;
		}
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("*", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.MULTIPLICATION);
			return result;
		}
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("/", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.DIVIDE);
			return result;
		}

		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("%", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.REST_FROM_DIVISION);
			return result;
		}

		// Operacja konwersji typ�w
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("$", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.CONVERT_TO_STRING);
			return result;
		}
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("#", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.CONVERT_TO_NUMBER);
			return result;
		}
		k = FormulaLexyconHelper.findLastSpecialWithBrecketIgnoring("~", buffor);
		if (k >= 0) {
			int[] result = { k, k + 1 };
			currentOperation.setType(FormulaOperationType.INVERT);
			return result;
		}
		return null;
	}

	public FormulaLeaf getOperationLeaf() {
		return operation;
	}

	public void setOperation(FormulaLeaf operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "FormulaLine [operation=" + operation + "]";
	}

	public FormulaInstruction copy() {
		return new FormulaLine(operation.getQuery());
	}

}

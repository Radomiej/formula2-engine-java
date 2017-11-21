package pl.radomiej.formula;

import pl.radomiej.formula.operations.FormulaOperationType;

public class FormulaLeaf {
	private String query;
	private FormulaLeaf left, right;
	private FormulaOperationType type;
	private boolean simpleQuery;
	
	public FormulaLeaf(String query) {
		this.query = query;
	}

	public FormulaOperationType getType() {
		return type;
	}

	public void setType(FormulaOperationType type) {
		this.type = type;
	}

	public FormulaLeaf getLeft() {
		return left;
	}

	public void setLeft(FormulaLeaf left) {
		this.left = left;
	}

	public FormulaLeaf getRight() {
		return right;
	}

	public void setRight(FormulaLeaf right) {
		this.right = right;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "FormulaLeaf [" + (query != null ? "query=" + query + ", " : "")
				+ (left != null ? "left=" + left + ", " : "") + (right != null ? "right=" + right + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + "simpleQuery=" + simpleQuery + "]";
	}

	public boolean isSimpleQuery() {
		return simpleQuery;
	}

	public void setSimpleQuery(boolean function) {
		this.simpleQuery = function;
	}

}

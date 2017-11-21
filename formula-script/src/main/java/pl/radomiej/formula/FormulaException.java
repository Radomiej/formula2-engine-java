package pl.radomiej.formula;

public class FormulaException extends Exception {

	private String query;

	public FormulaException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1648708687404538308L;

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
}

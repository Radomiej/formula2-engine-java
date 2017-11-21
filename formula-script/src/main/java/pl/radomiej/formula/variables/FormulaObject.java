package pl.radomiej.formula.variables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.radomiej.formula.FormulaVariable;

public class FormulaObject {
	private Map<String, FormulaVariable> fields = new LinkedHashMap<String, FormulaVariable>();

	public void setField(String fieldName, FormulaVariable fieldValue) {
		if(fieldName == null || fieldName.isEmpty()){
			System.err.println("Próba dodania pustego pola: " + fieldName);
			return;
		}
		getFields().put(fieldName, fieldValue);
	}

	public FormulaVariable getField(String fieldName) {
		FormulaVariable result = getFields().get(fieldName);
		if (result == null) {
			addField(fieldName);
			result = getFields().get(fieldName);
		}
		return result;
	}

	public FormulaVariable getField(int index) {
		if(index > getFields().size()) return FormulaVariable.getUndefined();
		
		int i = 1;
		for (FormulaVariable var : getFields().values()) {
			if (i == index) {
				return var;
			}
			i++;
		}
		
		return FormulaVariable.getUndefined();
	}

	public void addField(String fieldName) {
		if(fieldName == null || fieldName.isEmpty()){
			System.err.println("Próba dodania pustego pola: " + fieldName);
			return;
		}
		getFields().put(fieldName, FormulaVariable.getUndefined());
	}

	@Override
	public String toString() {
		return "FormulaObject [" + (fields != null ? "fields=" + fields : "") + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Not use directly, only for serialization!
	 * @return reference to internal fields map
	 */
	public Map<String, FormulaVariable> getFields() {
		return fields;
	}		
}

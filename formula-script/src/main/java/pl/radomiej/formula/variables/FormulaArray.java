package pl.radomiej.formula.variables;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.radomiej.formula.FormulaException;
import pl.radomiej.formula.FormulaVariable;

public class FormulaArray {
	public static List<String> asStringList(FormulaArray valueLikeArray) {
		List<String> results = new ArrayList<String>();
		for(FormulaVariable variable : valueLikeArray.getElements()){
			results.add(variable.getValueLikeString());
		}
		return results;
	}

	private List<FormulaVariable> elements = new ArrayList<FormulaVariable>();

	public void addVariable(FormulaVariable variableToAdd) {
		getElements().add(variableToAdd);
	}

	/**
	 * 
	 * @param index
	 *            indeks tablicy FORMULA zaczyna siê od 1
	 * @return zmienna na podanym indeksie
	 * @throws FormulaException 
	 */
	public FormulaVariable getVariable(int index) throws FormulaException {
		int javaIndex = index - 1;
		if(javaIndex >= elements.size() || javaIndex < 0){
			throw new FormulaException("Indeks " + index + " poza zakresem tablicy size: " + elements.size());
		}
		return getElements().get(javaIndex);
	}

	public int getSize() {
		return getElements().size();
	}

	public void clear() {
		getElements().clear();
	}

	/**
	 * 
	 * @return copy list of the current elements. Elements in the list aren`t copy.
	 */
	@JsonIgnore
	public List<FormulaVariable> getLikeJavaList() {
		return new ArrayList<FormulaVariable>(getElements());
	}

	@Override
	public String toString() {
		return "FormulaArray [" + (elements != null ? "elements=" + elements : "") + "]";
	}
	
	/**
	 * Not use directly, only for serialization!
	 * @return reference to internal list elements
	 */
	public List<FormulaVariable> getElements() {
		return elements;
	}

	/**
	 * 
	 * @param removeIndex index in FORMULA ARRAY: start from 1 to size
	 */
	public void removeAt(int removeIndex) {
		elements.remove(removeIndex - 1);
	}
}

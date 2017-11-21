package pl.radomiej.formula;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaDatetime;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

//TODO przemienienie klasy w niezmiennik i dodać optymalizacje dla pospolitych typów: UNDEFINED, 0, 1, -1, TRUE, FALSE etc.
public class FormulaVariable {
	public static FormulaVariable getUndefined() {
		return new FormulaVariable();
	}

	public static FormulaVariable getTrue() {
		return new FormulaVariable(1);
	}

	public static FormulaVariable getFalse() {
		return new FormulaVariable(0);
	}

	public static FormulaVariable getPositive() {
		return new FormulaVariable(1);
	}

	public static FormulaVariable getZero() {
		return new FormulaVariable(0);
	}

	public static FormulaVariable getNegative() {
		return new FormulaVariable(-1);
	}

	public static FormulaVariable getOne() {
		return new FormulaVariable(1);
	}

	private FormulaVariableType type = FormulaVariableType.UNDEFINED;
	private Object value;

	public FormulaVariable() {
	}

	public FormulaVariable(String stringVar) {
		value = stringVar;
		type = FormulaVariableType.STRING;
	}

	public FormulaVariable(int simpleInt) {
		value = new FormulaNumber(Integer.toString(simpleInt));
		type = FormulaVariableType.NUMBER;
	}

	public FormulaVariable(long simpleLong) {
		value = new FormulaNumber(Long.toString(simpleLong));
		type = FormulaVariableType.NUMBER;
	}

	public FormulaVariable(double simpleDouble) {
		value = new FormulaNumber(Double.toString(simpleDouble));
		type = FormulaVariableType.NUMBER;
	}

	public FormulaVariable(FormulaVariable initValue) {
		setFull(initValue);
	}

	public FormulaVariable(Object object) {
		setFullValue(object);
		// System.out.println("Object to FV: " + object);
	}

	public FormulaVariableType getType() {
		return type;
	}

	public void setType(FormulaVariableType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
			value = new FormulaNumber(value.toString());
			System.err.println("Niejawne przekazanie warto�ci typu liczbowego Javy. Nale�y u�y� metody setFullValue");
		}
		this.value = value;
	}

	public void setFullValue(Object objectToSetLikeValue) {
		if (objectToSetLikeValue == null)
			return;

		if (objectToSetLikeValue instanceof String) {
			type = FormulaVariableType.STRING;
			value = objectToSetLikeValue;
		} else if (objectToSetLikeValue instanceof FormulaFunction) {
			type = FormulaVariableType.FUNCTION;
			value = objectToSetLikeValue;
		}  else if (objectToSetLikeValue instanceof FormulaVariable) {
			setFull((FormulaVariable) objectToSetLikeValue);
		} else if (objectToSetLikeValue instanceof FormulaDatetime) {
			type = FormulaVariableType.DATE_TIME;
			value = objectToSetLikeValue;
		} else if (objectToSetLikeValue instanceof Integer || objectToSetLikeValue instanceof Long
				|| objectToSetLikeValue instanceof Float || objectToSetLikeValue instanceof Double
				|| objectToSetLikeValue instanceof BigDecimal) {
			type = FormulaVariableType.NUMBER;
			value = new FormulaNumber(objectToSetLikeValue.toString());
		} else if (objectToSetLikeValue instanceof FormulaNumber) {
			type = FormulaVariableType.NUMBER;
			value = objectToSetLikeValue;
		} else if (objectToSetLikeValue instanceof FormulaObject) {
			type = FormulaVariableType.OBJECT;
			value = objectToSetLikeValue;
		} else if (objectToSetLikeValue instanceof FormulaArray) {
			type = FormulaVariableType.ARRAY;
			value = objectToSetLikeValue;
		} else if (objectToSetLikeValue instanceof Boolean) {
			type = FormulaVariableType.NUMBER;
			int booleanValue = ((Boolean) objectToSetLikeValue) ? 1 : 0;
			value = new FormulaNumber(Integer.toString(booleanValue));
		}  else {
			type = FormulaVariableType.STRING;
			value = objectToSetLikeValue.toString();
		}
		
	}

	public void setFull(FormulaVariable right) {
		type = right.type;
		setFullValue(right.value);
	}

	@Override
	public String toString() {
		return "FormulaVariable [" + (type != null ? "type=" + type + ", " : "")
				+ (value != null ? "value=" + value : "") + "]";
	}

	public FormulaVariable copy() {
		return new FormulaVariable(this);
	}

	/**
	 * Pobiera value jako konkretny typ.
	 * 
	 * @return FormulaNumber je�li value jest tego typu lub jest STRING`em z
	 *         mo�liwo�ci� sparsowania na typ NUMBER. W przeciwnym wypadku
	 *         zwraca NULL
	 */
	@JsonIgnore
	public FormulaNumber getValueLikeNumber() {
		if (value instanceof FormulaNumber)
			return (FormulaNumber) value;
		else if (value instanceof String && NumberUtils.isCreatable(getValueLikeString())) {
			FormulaNumber number = new FormulaNumber(getValueLikeString());
			return number;
		}
		return null;
	}

	/**
	 * Pobiera value jako konkretny typ.
	 * 
	 * @return STRING je�li value jest r�ne od UNDEFINED.
	 */
	@JsonIgnore
	public String getValueLikeString() {
		if (type == FormulaVariableType.UNDEFINED)
			return null;
		if (value instanceof String)
			return value.toString();
		return value.toString();
	}

	/**
	 * Pobiera value jako konkretny typ.
	 * 
	 * @return FormulaObject je�li value jest tego typu lub null je�li jest
	 *         innego typu.
	 */
	@JsonIgnore
	public FormulaObject getValueLikeObject() {
		if (value instanceof FormulaObject)
			return (FormulaObject) value;
		return null;
	}

	public FormulaArray getValueLikeArray() {
		if (value instanceof FormulaArray)
			return (FormulaArray) value;
		return null;
	}

	public boolean getValueLikeBoolean() {
		if (value == null)
			return false;
		if (value instanceof FormulaNumber && ((FormulaNumber) value).getBigDecimalValue().compareTo(BigDecimal.ZERO) != 0)
			return true;
		else if (value instanceof String && !value.toString().isEmpty())
			return true;

		return false;
	}

	public FormulaDatetime getValueLikeDateTime() {
		if(getType() == FormulaVariableType.DATE_TIME){
			FormulaDatetime datetime = (FormulaDatetime) value;
			return datetime;
		}
		return null;
	}

	public static List<FormulaVariable> asList(FormulaVariable... variables) {
		List<FormulaVariable> results = new ArrayList<FormulaVariable>();
		for(FormulaVariable variable : variables){
			results.add(variable);
		}
		return results;
	}

}

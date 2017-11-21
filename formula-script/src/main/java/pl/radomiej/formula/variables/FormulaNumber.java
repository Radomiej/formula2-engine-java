package pl.radomiej.formula.variables;

import java.math.BigDecimal;

public class FormulaNumber {
	private static final BigDecimal zero = new BigDecimal("0.0");
	private BigDecimal bigDecimalValue;
	
	public FormulaNumber(String query) {
		if(!query.contains(".")) query += ".0";
//		if(query.equals("0")) query = "0.0";
		bigDecimalValue = new BigDecimal(query);
	}

	public FormulaNumber(BigDecimal bigDecimalValue2) {
		bigDecimalValue = new BigDecimal(bigDecimalValue2.toString());
	}

	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}

	public BigDecimal add(FormulaNumber value) {
		return bigDecimalValue.add(value.bigDecimalValue);
	}

	public BigDecimal subtract(FormulaNumber value) {
		return bigDecimalValue.subtract(value.bigDecimalValue);
	}

	public BigDecimal multiply(FormulaNumber value) {
		return bigDecimalValue.multiply(value.bigDecimalValue);
	}

	public BigDecimal divide(FormulaNumber value) {
		if(value.getBigDecimalValue().equals(zero)){
			return null;
		}
		return bigDecimalValue.divide(value.bigDecimalValue);
	}

	@Override
	public String toString() {
		return bigDecimalValue.toString();
	}

	public int getInt() {
		return bigDecimalValue.intValue();
	}

	public double getDouble() {
		return bigDecimalValue.doubleValue();
	}

	public long getLong() {
		return bigDecimalValue.longValue();
	}

	public float getFloat() {
		return bigDecimalValue.floatValue();
	}
	
	public Integer getIntObj() {
		return bigDecimalValue.intValue();
	}

	public Double getDoubleObj() {
		return bigDecimalValue.doubleValue();
	}

	public Long getLongObj() {
		return bigDecimalValue.longValue();
	}

	public Float getFloatObj() {
		return bigDecimalValue.floatValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bigDecimalValue == null) ? 0 : bigDecimalValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormulaNumber other = (FormulaNumber) obj;
		if (bigDecimalValue == null) {
			if (other.bigDecimalValue != null)
				return false;
		} else if (bigDecimalValue.compareTo(other.bigDecimalValue) != 0)
			return false;
		return true;
	}

}

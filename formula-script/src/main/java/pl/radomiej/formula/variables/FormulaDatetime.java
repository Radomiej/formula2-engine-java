package pl.radomiej.formula.variables;

import java.util.Date;

import org.joda.time.DateTime;

public class FormulaDatetime {
	private DateTime dateTime;

	public FormulaDatetime() {
		dateTime = new DateTime();
	}

	public FormulaDatetime(int year, int month, int day, int hour, int minute, int secound) {
		dateTime = new DateTime(year, month, day, hour, minute, secound);
	}

	public FormulaDatetime(Date initDate) {
		dateTime = new DateTime(initDate);
	}

	public FormulaDatetime(DateTime time) {
		dateTime = new DateTime(time);
	}

	public FormulaDatetime(long time) {
		dateTime = new DateTime(time);
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "FormulaDatetime [dateTime=" + dateTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
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
		FormulaDatetime other = (FormulaDatetime) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		return true;
	}
}

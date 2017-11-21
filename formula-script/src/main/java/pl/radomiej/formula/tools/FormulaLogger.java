package pl.radomiej.formula.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormulaLogger {
	public static FormulaLogger getLogger(Class clazz) {
		return new FormulaLogger(clazz);
	}

	private String className;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss:SSS");

	public FormulaLogger(Class clazz) {
		this.className = clazz.getSimpleName();
	}

	public void log(String message) {
		log(message, null);
	}

	public void log(String message, Throwable throwable) {
		Date date = new Date(System.currentTimeMillis());
		String timePart = df.format(date);
		String throwablePart = throwable == null ? "" : throwable.getMessage();
		String outPart = timePart + " " + message + " " + throwablePart;
		System.out.println(outPart);
	}

	public void error(String message) {
		error(message, null);
	}

	public void error(String message, Throwable throwable) {
		Date date = new Date(System.currentTimeMillis());
		String timePart = df.format(date);
		String throwablePart = throwable == null ? "" : throwable.getMessage();
		String outPart = timePart + " " + message + " " + throwablePart;
		System.err.println(outPart);
	}
}

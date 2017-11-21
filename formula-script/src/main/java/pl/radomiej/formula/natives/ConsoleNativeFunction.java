package pl.radomiej.formula.natives;

import java.io.PrintStream;
import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.NativeFormulaMethod;

public class ConsoleNativeFunction implements NativeFormulaMethod {
	private PrintStream printStream = System.out;
	
	
	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		if(args.size() > 0) printStream.println(args.get(0).getValue());
		return new FormulaVariable(1);
	}


	public PrintStream getPrintStream() {
		return printStream;
	}


	public void setPrintStream(PrintStream printStream) {
		this.printStream = printStream;
	}

}

package pl.radomiej.formula;

import java.util.List;

public interface NativeFormulaMethod {
	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args);
}

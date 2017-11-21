package pl.radomiej.formula;

public interface LocalMemory {
	public FormulaVariable getLocalVariable(String variableName);
	public boolean existLocalVariable(String variableName);
}

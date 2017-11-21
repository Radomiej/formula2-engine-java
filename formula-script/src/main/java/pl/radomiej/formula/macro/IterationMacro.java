package pl.radomiej.formula.macro;

import java.util.ArrayList;
import java.util.List;

import pl.radomiej.formula.FormulaInstruction;
import pl.radomiej.formula.FormulaMacro;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.LocalMemory;

public class IterationMacro extends FormulaMacro implements LocalMemory{
	private List<FormulaInstruction> condition = new ArrayList<FormulaInstruction>();
	private List<FormulaInstruction> initLoop = new ArrayList<FormulaInstruction>();
	private FormulaVariable loopCounter;
	private String loopCounterName;
	
	public List<FormulaInstruction> getCondition() {
		return condition;
	}

	public void setCondition(List<FormulaInstruction> condition) {
		this.condition = condition;
	}

	public List<FormulaInstruction> getInitLoop() {
		return initLoop;
	}

	public void setInitLoop(List<FormulaInstruction> initLoop) {
		this.initLoop = initLoop;
	}

	public FormulaVariable getLoopCounter() {
		return loopCounter;
	}

	public void setLoopCounter(FormulaVariable loopCounter) {
		this.loopCounter = loopCounter;
	}

	public void setLoopCounterName(String localIteratorVariableName) {
		this.loopCounterName = localIteratorVariableName;
	}
	
	@Override
	public FormulaVariable getLocalVariable(String variableName) {
		if(variableName.equals(loopCounterName)){
			return loopCounter;
		}
		return super.getOrCreateLocalVariable(variableName);
	}
	
	@Override
	public FormulaMacro copy() {
		IterationMacro cop = new IterationMacro();
//		cop.setParent(parentMemory);
		for (FormulaInstruction instruction : getInstructions()) {
			cop.getInstructions().add(instruction.copy());
		}
		for (FormulaInstruction instruction : initLoop) {
			cop.initLoop.add(instruction.copy());
		}
		for (FormulaInstruction instruction : condition) {
			cop.condition.add(instruction.copy());
		}
		
		return cop;
	}

}

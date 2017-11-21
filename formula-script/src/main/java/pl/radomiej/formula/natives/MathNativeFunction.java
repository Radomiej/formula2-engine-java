package pl.radomiej.formula.natives;

import java.util.List;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaNumber;

public class MathNativeFunction implements NativeFormulaMethod {
	private MathStrategy mathStrategy;
	private boolean rangeMath = false;
	private double minX, maxX;

	public MathNativeFunction(MathStrategy mathStrategy) {
		this.mathStrategy = mathStrategy;
	}

	public MathNativeFunction(MathStrategy mathStrategy, double minX, double maxX) {
		this.mathStrategy = mathStrategy;
		rangeMath = true;
		this.minX = minX;
		this.maxX = maxX;
	}

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		FormulaVariable first = args.get(0);
		FormulaNumber valueToCompute = (FormulaNumber) first.getValue();
		double valueX = valueToCompute.getDouble();
		double valueY = 0;
		if(args.size() > 1) valueY = args.get(1).getValueLikeNumber().getDouble();
		
		if (rangeMath && (valueX < minX || valueX > maxX)) {
			return new FormulaVariable("*** Argument spoza dziedziny funkcji ***");
		}

		double result = mathStrategy.doMath(valueX, valueY);

		// if(args.size() > 0) System.out.println("sin: " +
		// args.get(0).getValue() + " result: " + result);
		return new FormulaVariable(result);
	}

}
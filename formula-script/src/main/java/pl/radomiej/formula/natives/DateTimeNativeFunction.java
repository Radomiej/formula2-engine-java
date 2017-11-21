package pl.radomiej.formula.natives;

import java.util.List;

import org.joda.time.DateTime;

import pl.radomiej.formula.FormulaEngine;
import pl.radomiej.formula.FormulaVariable;
import pl.radomiej.formula.FormulaVariableType;
import pl.radomiej.formula.NativeFormulaMethod;
import pl.radomiej.formula.variables.FormulaArray;
import pl.radomiej.formula.variables.FormulaDatetime;
import pl.radomiej.formula.variables.FormulaNumber;
import pl.radomiej.formula.variables.FormulaObject;

public class DateTimeNativeFunction implements NativeFormulaMethod {

	public FormulaVariable invoke(FormulaEngine engine, List<FormulaVariable> args) {
		DateTime current = new DateTime();
		if(args.size() == 0){
			return new FormulaVariable(new FormulaDatetime());
		}else{
			int year = args.get(0).getValueLikeNumber().getInt();
			int month = current.getMonthOfYear(), day = current.getDayOfMonth(), hour = current.getHourOfDay(), minute = current.getMinuteOfHour(), secound = current.getSecondOfMinute();
			if(args.size() >= 2) month = args.get(1).getValueLikeNumber().getInt();
			if(args.size() >= 3) day = args.get(2).getValueLikeNumber().getInt();
			if(args.size() >= 4) hour = args.get(3).getValueLikeNumber().getInt();
			if(args.size() >= 5) minute = args.get(4).getValueLikeNumber().getInt();
			if(args.size() >= 6) secound = args.get(5).getValueLikeNumber().getInt();
			return new FormulaVariable(new FormulaDatetime(year, month, day, hour, minute, secound));
		}

//		throw new UnsupportedOperationException("Niewspierana forma tworzenia obiektu");
	}

	

}
package pl.radomiej.formula.natives;

import org.apache.commons.lang3.math.NumberUtils;

public enum MathStrategy {
	SIN {
		@Override
		public double doMath(double x, double y) {
			return Math.sin(x);
		}
	}, SINH {
		@Override
		public double doMath(double x, double y) {
			return Math.sinh(x);
		}
	}, COS {
		@Override
		public double doMath(double x, double y) {
			return Math.cos(x);
		}
	}, COSH {
		@Override
		public double doMath(double x, double y) {
			return Math.cosh(x);
		}
	}, ASIN {
		@Override
		public double doMath(double x, double y) {
			return Math.asin(x);
		}
	}, ACOS {
		@Override
		public double doMath(double x, double y) {
			return Math.acos(x);
		}
	}, TAN {
		@Override
		public double doMath(double x, double y) {
			return Math.tan(x);
		}
	}, TANH {
		@Override
		public double doMath(double x, double y) {
			return Math.tanh(x);
		}
	}, ATAN {
		@Override
		public double doMath(double x, double y) {
			return Math.atan(x);
		}
	}, ATAN2 {
		@Override
		public double doMath(double x, double y) {
			return Math.atan2(y, x);
		}
	}, CEIL {
		@Override
		public double doMath(double x, double y) {
			return Math.acos(x);
		}
	}, EXP {
		@Override
		public double doMath(double x, double y) {
			return Math.exp(x);
		}
	}, FABS {
		@Override
		public double doMath(double x, double y) {
			return Math.abs(x);
		}
	}, FLOOR {
		@Override
		public double doMath(double x, double y) {
			return Math.floor(x);
		}
	}, FRAC {
		@Override
		public double doMath(double x, double y) {
			return x - Math.floor(x);
		}
	}, INT {
		@Override
		public double doMath(double x, double y) {
			return (long) x;
		}
	}, LOG {
		@Override
		public double doMath(double x, double y) {
			return Math.log(x);
		}
	}, LOG10 {
		@Override
		public double doMath(double x, double y) {
			return Math.log10(x);
		}
	}, POW {
		@Override
		public double doMath(double x, double y) {
			return Math.pow(x, y);
		}
	}, SQRT {
		@Override
		public double doMath(double x, double y) {
			return Math.sqrt(x);
		}
	};
	
	
	
	public abstract double doMath(double x, double y);
}

package gameSystems.scriptingSystem.systemScripts;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptDouble;

public class ScriptMath extends EScript {

	public ScriptMath() throws ScriptError {
		super("Math");
		
		//add global variable values
		staticVariables.add(new ScriptDouble("PI", Math.PI));
		staticVariables.add(new ScriptDouble("E", Math.E));
		
		//add math functions
		//functions.add(new MaxD());
		//functions.add(new MaxI());
		//functions.add(new MinD());
		//functions.add(new MinI());
		//abs
		//cos
		//sin
		//tan
		//acos
		//asin
		//atan
		//floor
		//ceil
		//log
		//pow
		//random
		//sqrt
	}
	
	/*
	private class MaxD extends ScriptFunction {
		protected MaxD() { super(ScriptMath.this, null, EDataType.DOUBLE, "max", new EArrayList(EDataType.DOUBLE, EDataType.DOUBLE), 1); }
		
		@Override
		public Object evaluate(Object... args) throws ScriptError {
			try {
				double a = (double) args[0];
				double b = (double) args[1];
				
				return Math.max(a, b);
			}
			catch (Exception e) { e.printStackTrace(); }
			return args[0];
		}
	}
	
	private class MaxI extends ScriptFunction {
		protected MaxI() { super(ScriptMath.this, null, EDataType.INT, "max", new EArrayList(EDataType.INT, EDataType.INT), 1); }
		
		@Override
		public Object evaluate(Object... args) throws ScriptError {
			try {
				int a = (int) args[0];
				int b = (int) args[1];
				
				return Math.max(a, b);
			}
			catch (Exception e) { e.printStackTrace(); }
			return args[0];
		}
	}
	
	private class MinD extends ScriptFunction {
		protected MinD() { super(ScriptMath.this, null, EDataType.DOUBLE, "min", new EArrayList(EDataType.DOUBLE, EDataType.DOUBLE), 1); }
		
		@Override
		public Object evaluate(Object... args) throws ScriptError {
			try {
				double a = (double) args[0];
				double b = (double) args[1];
				
				return Math.min(a, b);
			}
			catch (Exception e) { e.printStackTrace(); }
			return args[0];
		}
	}
	
	private class MinI extends ScriptFunction {
		protected MinI() { super(ScriptMath.this, null, EDataType.INT, "min", new EArrayList(EDataType.INT, EDataType.INT), 1); }
		
		@Override
		public Object evaluate(Object... args) throws ScriptError {
			try {
				int a = (int) args[0];
				int b = (int) args[1];
				
				return Math.min(a, b);
			}
			catch (Exception e) { e.printStackTrace(); }
			return args[0];
		}
	}
	
	*/
}

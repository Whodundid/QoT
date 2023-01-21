package envisionEngine.debug;

import envisionEngine.debug.debugCommands.Deb0;
import envisionEngine.debug.debugCommands.Deb1;
import envisionEngine.debug.debugCommands.Deb2;
import envisionEngine.debug.debugCommands.Deb3;
import envisionEngine.debug.debugCommands.Deb4;
import envisionEngine.debug.debugCommands.Deb5;
import envisionEngine.debug.debugCommands.Deb6;
import envisionEngine.debug.debugCommands.Deb7;
import envisionEngine.debug.debugCommands.Deb8;
import envisionEngine.debug.debugCommands.Deb9;
import envisionEngine.debug.debugCommands.DebugCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

@SuppressWarnings("unused")

/** A basic utility that allows the execution of dev defined test java code within game. */
public class DebugFunctions {

	private static EArrayList<DebugCommand> commands = new EArrayList();
	
	static {
		commands.add(new Deb0());
		commands.add(new Deb1());
		commands.add(new Deb2());
		commands.add(new Deb3());
		commands.add(new Deb4());
		commands.add(new Deb5());
		commands.add(new Deb6());
		commands.add(new Deb7());
		commands.add(new Deb8());
		commands.add(new Deb9());
	}
	
	//these are settings which can be toggled in game to hide/display certain debug values
	public static boolean drawInfo = true;
	public static boolean drawWindowPID = true;
	public static boolean drawWindowInit = false;
	public static boolean drawWindowDimensions = false;
	
	//a random value that can be used to store doubles. Doesn't have an intended purpose outside of testing.
	public static double val = 0;
	public static Object tempObj0 = null;
	public static Object tempObj1 = null;
	public static Object tempObj2 = null;
	public static Object tempObj3 = null;
	public static Object tempObj4 = null;
	public static Object tempObj5 = null;
	public static Object tempObj6 = null;
	public static Object tempObj7 = null;
	public static Object tempObj8 = null;
	public static Object tempObj9 = null;
	
	/** Runs the debug function that pertains to the given IDebugCommand. */
	public static void runDebugFunction(IDebugCommand function) { runDebugFunction(function.getDebugCommandID(), null); }
	/** Runs the debug function number. */
	public static boolean runDebugFunction(int functionID) { return runDebugFunction(functionID, null); }
	/** Runs the debug function number with given arguments inside of an EArrayList. */
	public static boolean runDebugFunction(int functionID, EArrayList args) {
		return runDebugFunction(functionID, null, EUtil.nullApplyR(args, a -> a.toArray(), new Object[0]));
	}
	/** Runs the debug function number with the option to specify a terminal and a series of arguments. */
	public static boolean runDebugFunction(int functionID, ETerminal termIn, Object... args) {
		try {
			DebugCommand c = commands.get(functionID);
			c.run(termIn, args);
			return true;
		}
		catch (Throwable e) { e.printStackTrace(); }
		return false;
	}
	
	public static DebugCommand getDebugFunction(IDebugCommand id) { return getDebugFunction(id.getDebugCommandID()); }
	public static DebugCommand getDebugFunction(int functionID) {
		if (functionID < 0 || functionID >= commands.size()) return null;
		return commands.get(functionID);
	}
	
	/** Returns the total number of debug functions available. */
	public static int getTotal() { return IDebugCommand.values().length; }
	
	public static void renderUpdate() { commands.forEach(c -> c.onRendererUpdate()); }
	
}

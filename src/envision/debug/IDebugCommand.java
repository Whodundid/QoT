package envision.debug;

//Author: Hunter Bragg

/** An enum pertaining to a list of available debug functions. */
public enum IDebugCommand {
	DEBUG_0(0),
	DEBUG_1(1),
	DEBUG_2(2),
	DEBUG_3(3),
	DEBUG_4(4),
	DEBUG_5(5),
	DEBUG_6(6),
	DEBUG_7(7),
	DEBUG_8(8),
	DEBUG_9(9);
	
	private int id = -1;
	
	IDebugCommand(int idIn) {
		id = idIn;
	}
	
	public int getDebugCommandID() { return id; }
	public String getName() { return getDebugCommandName(this); }
	
	public static String getDebugCommandName(IDebugCommand commandIn) {
		return (commandIn != null) ? "Deb " + commandIn.getDebugCommandID() : "Null Debug Command!";
	}
	
}

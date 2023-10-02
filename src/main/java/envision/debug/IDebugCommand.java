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
	DEBUG_9(9),
	DEBUG_10(10),
	DEBUG_11(11),
	DEBUG_12(12),
	DEBUG_13(13),
	DEBUG_14(14),
	DEBUG_15(15),
	DEBUG_16(16),
	DEBUG_17(17),
	DEBUG_18(18),
	DEBUG_19(19);
	
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

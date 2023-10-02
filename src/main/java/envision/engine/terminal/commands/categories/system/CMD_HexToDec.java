package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_HexToDec extends TerminalCommand {
	
	public CMD_HexToDec() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "hextodec"; }
	@Override public EList<String> getAliases() { return EList.of("h2d", "hex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "converts a hex value to a decimal value"; }
	@Override public String getUsage() { return "ex: h2d 0xff"; }
	
	@Override
	public void runCommand() {
	    expectAtLeast(1);
	    
		String s = firstArg();
		s = (s.startsWith("0x")) ? s.substring(2) : s;
		
		System.out.println(0xff00ffff);
		
		long val = Long.parseLong(s, 16);
		writeln(val, EColors.aquamarine);
	}
	
}
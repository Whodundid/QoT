package envision.debug.terminal.commands.categories.system;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_HexToDec extends TerminalCommand {
	
	public CMD_HexToDec() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "hextodec"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("h2d", "hex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "converts a hex value to a decimal value"; }
	@Override public String getUsage() { return "ex: h2d 0xff"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.info(getUsage()); }
		
		String s = args.get(0);
		s = (s.startsWith("0x")) ? s.substring(2) : s;
		
		System.out.println(0xff00ffff);
		
		long val = Long.parseLong(s, 16);
		termIn.writeln(val, EColors.aquamarine);
	}
	
}
package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

public class HexToDec extends TerminalCommand {
	
	public HexToDec() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}
	
	@Override public String getName() { return "hextodec"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("h2d", "hex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "converts a hex value to a decimal value"; }
	@Override public String getUsage() { return "ex: h2d 0xff"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.info(getUsage()); }
		
		String s = args.get(0);
		s = (s.startsWith("0x")) ? s.substring(2) : s;
		
		System.out.println(0xff00ffff);
		
		long val = Long.parseLong(s, 16);
		termIn.writeln(val, EColors.aquamarine);
	}
	
}
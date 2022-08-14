package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import main.QoT;

public class SetWorldUnderground_CMD extends TerminalCommand {
	
	public SetWorldUnderground_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "set_underground"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("setunder"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Modifies whether or not the current world is underground or not."; }
	@Override public String getUsage() { return "ex: set_underground true"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.length() != 1) termIn.error(getUsage());
		
		String val = args.get(0).toLowerCase();
		
		if (val.equals("true") || val.equals("t")) QoT.theWorld.setUnderground(true);
		else if (val.equals("false") || val.equals("f")) QoT.theWorld.setUnderground(false);
		else termIn.error(getUsage());
	}
}

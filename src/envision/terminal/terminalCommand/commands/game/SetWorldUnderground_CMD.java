package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

public class SetWorldUnderground_CMD extends TerminalCommand {
	
	public SetWorldUnderground_CMD() {
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "set_underground"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("setunder"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Modifies whether or not the current world is underground or not."; }
	@Override public String getUsage() { return "ex: set_underground true"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.length() != 1) {
			errorUsage(termIn, ERROR_NOT_ENOUGH);
			return;
		}
		
		if (QoT.theWorld == null) {
			termIn.error("No world loaded!");
			return;
		}
		
		String val = args.get(0).toLowerCase();
		
		if (val.equals("true") || val.equals("t")) QoT.theWorld.setUnderground(true);
		else if (val.equals("false") || val.equals("f")) QoT.theWorld.setUnderground(false);
		else termIn.error(getUsage());
	}
}

package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class SetWorldUnderground_CMD extends TerminalCommand {
	
	public SetWorldUnderground_CMD() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "underground"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("under"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Modifies whether or not the current world is underground or not."; }
	@Override public String getUsage() { return "ex: underground"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (QoT.theWorld == null) {
			termIn.error("No world loaded!");
			return;
		}
		
		//empty -- toggle
		if (args.length() == 0) {
			QoT.theWorld.setUnderground(!QoT.theWorld.isUnderground());
			return;
		}
		
		if (args.length() != 1) {
			errorUsage(termIn, ERROR_NOT_ENOUGH);
			return;
		}
		
		
		String val = args.get(0).toLowerCase();
		
		if (val.equals("true") || val.equals("t")) QoT.theWorld.setUnderground(true);
		else if (val.equals("false") || val.equals("f")) QoT.theWorld.setUnderground(false);
		else termIn.error(getUsage());
	}
}

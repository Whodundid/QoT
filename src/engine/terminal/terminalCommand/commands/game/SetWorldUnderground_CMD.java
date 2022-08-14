package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import main.QoT;

public class SetWorldUnderground_CMD extends TerminalCommand {
	
	public SetWorldUnderground_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 1;
	}

	@Override public String getName() { return "set_underground"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("setunder"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Modifies whether or not the current world is underground or not."; }
	@Override public String getUsage() { return "ex: set_underground true"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			var under = QoT.theWorld.isUnderground();
			termIn.writeln("Set " + ((under) ? "above ground" : "underground"), EColors.yellow);
			QoT.theWorld.setUnderground(!under);
		}
		else if (args.length() > 1) {
			termIn.errorUsage(ERROR_TOO_MANY, getUsage());
		}
		
		String val = args.get(0).toLowerCase();
		
		if (val.equals("true") || val.equals("t")) QoT.theWorld.setUnderground(true);
		else if (val.equals("false") || val.equals("f")) QoT.theWorld.setUnderground(false);
		else termIn.error(getUsage());
	}
}

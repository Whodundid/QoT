package engine.terminal.terminalCommand.commands.game;

import engine.QoT;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

public class SaveWorld_CMD extends TerminalCommand {
	
	public SaveWorld_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "saveworld"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("sw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Saves the current world (if any)"; }
	@Override public String getUsage() { return "ex: sw"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (QoT.theWorld != null) {
			termIn.writeln("Saving '" + QoT.theWorld.getName() + "'", EColors.green);
			QoT.theWorld.saveWorldToFile();
		}
		else {
			termIn.writeln("No world to save", EColors.yellow);
		}
	}
	
}

package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

public class SaveWorld_CMD extends TerminalCommand {
	
	public SaveWorld_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "saveworld"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("sw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Saves the current world (if any)"; }
	@Override public String getUsage() { return "ex: sw"; }
	
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

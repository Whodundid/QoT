package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class SaveWorld_CMD extends TerminalCommand {
	
	public SaveWorld_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "saveworld"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("sw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Saves the current world (if any)"; }
	@Override public String getUsage() { return "ex: sw"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (QoT.theWorld != null) {
			termIn.writeln("Saving '" + QoT.theWorld.getWorldName() + "'", EColors.green);
			QoT.theWorld.saveWorldToFile();
		}
		else {
			termIn.writeln("No world to save", EColors.yellow);
		}
	}
	
}

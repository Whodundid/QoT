package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class CMD_SaveWorld extends TerminalCommand {
	
	public CMD_SaveWorld() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "saveworld"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("sw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Saves the current world (if any)"; }
	@Override public String getUsage() { return "ex: sw"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (QoT.theWorld != null) {
			termIn.writeln("Saving '" + QoT.theWorld.getWorldName() + "'", EColors.green);
			QoT.theWorld.saveWorldToFile();
		}
		else {
			termIn.writeln("No world to save", EColors.yellow);
		}
	}
	
}

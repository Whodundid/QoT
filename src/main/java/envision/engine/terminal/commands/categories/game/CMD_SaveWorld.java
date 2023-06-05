package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

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
		if (Envision.theWorld != null) {
			termIn.writeln("Saving '" + Envision.theWorld.getWorldName() + "'", EColors.green);
			Envision.theWorld.saveWorldToFile();
		}
		else {
			termIn.writeln("No world to save", EColors.yellow);
		}
	}
	
}

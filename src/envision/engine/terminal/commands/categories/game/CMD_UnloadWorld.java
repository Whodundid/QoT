package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.screens.main.MainMenuScreen;

public class CMD_UnloadWorld extends TerminalCommand {
	
	public CMD_UnloadWorld() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "unloadworld"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("uw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Unloads the current world (if any)"; }
	@Override public String getUsage() { return "ex: uw"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (Envision.theWorld != null) {
			termIn.writeln("Unloading world '" + Envision.theWorld.getWorldName() + "'", EColors.green);
			Envision.loadWorld(null);
			Envision.displayScreen(new MainMenuScreen());
		}
		else {
			termIn.writeln("No world to unload", EColors.yellow);
		}
	}
	
}

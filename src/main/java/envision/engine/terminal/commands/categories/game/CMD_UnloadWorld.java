package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.screens.main.MainMenuScreen;

public class CMD_UnloadWorld extends TerminalCommand {
	
	public CMD_UnloadWorld() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "unloadworld"; }
	@Override public EList<String> getAliases() { return EList.of("uw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Unloads the current world (if any)"; }
	@Override public String getUsage() { return "ex: uw"; }
	
	@Override
	public Object runCommand() {
		if (Envision.theWorld == null) {
		    writeln("No world to unload", EColors.yellow);
		    return null;
		}
		
		writeln("Unloading world '" + Envision.theWorld.getWorldName() + "'", EColors.green);
        Envision.loadWorld(null);
        displayScreen(new MainMenuScreen());
        return null;
	}
	
}

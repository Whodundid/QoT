package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;
import qot.screens.main.MainMenuScreen;

public class UnloadWorld_CMD extends TerminalCommand {
	
	public UnloadWorld_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "unloadworld"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("uw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Unloads the current world (if any)"; }
	@Override public String getUsage() { return "ex: uw"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (QoT.theWorld != null) {
			termIn.writeln("Unloading world '" + QoT.theWorld.getWorldName() + "'", EColors.green);
			QoT.loadWorld(null);
			QoT.displayScreen(new MainMenuScreen());
		}
		else {
			termIn.writeln("No world to unload", EColors.yellow);
		}
	}
	
}

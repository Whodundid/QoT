package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

public class World_CMD extends TerminalCommand {
	
	public World_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "world"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays current world info"; }
	@Override public String getUsage() { return "ex: world"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			errorUsage(termIn, ERROR_NO_ARGS);
			return;
		}
		
		GameWorld world = QoT.theWorld;
		
		if (world == null) {
			termIn.error("No world loaded!");
		}
		else {
			String n = world.getWorldName();
			int w = world.getWidth();
			int h = world.getHeight();
			boolean u = world.isUnderground();
			
			termIn.writeln(EColors.yellow + "Name: " + EColors.green + n);
			termIn.writeln(EColors.yellow + "Dims: " + EColors.green + "[" + w + "x" + h + "]");
			termIn.writeln(EColors.yellow + "Underground: " + EColors.green + u);
		}
	}
	
}

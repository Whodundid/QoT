package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.game.world.GameWorld;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

public class CMD_WorldInfo extends TerminalCommand {
	
	public CMD_WorldInfo() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "world"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays current world info"; }
	@Override public String getUsage() { return "ex: world"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
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

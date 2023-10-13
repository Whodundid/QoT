package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

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
	public void runCommand() {
	    expectNoArgs();
		
		IGameWorld world = Envision.theWorld;
		
		if (world == null) {
			error("No world loaded!");
			return;
		}
		
		String n = world.getWorldName();
        int w = world.getWidth();
        int h = world.getHeight();
        boolean u = world.isUnderground();
        
        writeln(EColors.yellow + "Name: " + EColors.green + n);
        writeln(EColors.yellow + "Dims: " + EColors.green + "[" + w + "x" + h + "]");
        writeln(EColors.yellow + "Underground: " + EColors.green + u);
	}
	
}

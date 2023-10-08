package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;

public class CMD_PauseGame extends TerminalCommand {
	
	public CMD_PauseGame() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "pause"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pauses or resumes the game"; }
	@Override public String getUsage() { return "ex: pause"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
	    
		boolean paused = Envision.isPaused();
		
		if (paused) {
			Envision.unpause();
			writeln("Game Resumed", EColors.yellow);
		}
		else {
			Envision.pause();
			writeln("Game Paused", EColors.yellow);
		}
		
		return null;
	}
	
}

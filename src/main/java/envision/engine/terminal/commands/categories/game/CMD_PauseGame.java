package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_PauseGame extends TerminalCommand {
	
	public CMD_PauseGame() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "pause"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pauses or resumes the game"; }
	@Override public String getUsage() { return "ex: pause"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.error("This command takes no arguments!");
			return;
		}
		
		var paused = Envision.isPaused();
		
		if (paused) {
			Envision.unpause();
			termIn.writeln("Game Resumed", EColors.yellow);
		}
		else {
			Envision.pause();
			termIn.writeln("Game Paused", EColors.yellow);
		}
	}
	
}

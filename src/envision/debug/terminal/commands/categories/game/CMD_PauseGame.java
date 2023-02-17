package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

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
		
		var paused = QoT.isPaused();
		
		if (paused) {
			QoT.unpause();
			termIn.writeln("Game Resumed", EColors.yellow);
		}
		else {
			QoT.pause();
			termIn.writeln("Game Paused", EColors.yellow);
		}
	}
	
}

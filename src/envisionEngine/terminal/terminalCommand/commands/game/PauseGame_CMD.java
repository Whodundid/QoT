package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

public class PauseGame_CMD extends TerminalCommand {
	
	public PauseGame_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "pause"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pauses or resumes the game"; }
	@Override public String getUsage() { return "ex: pause"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
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

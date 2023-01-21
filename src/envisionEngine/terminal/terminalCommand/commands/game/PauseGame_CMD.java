package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

public class PauseGame_CMD extends TerminalCommand {
	
	public PauseGame_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "pause"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pauses or resumes the game"; }
	@Override public String getUsage() { return "ex: pause"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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

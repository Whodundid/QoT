package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EList;
import main.QoT;

public class PauseGame_CMD extends TerminalCommand {
	
	public PauseGame_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "pause"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Pauses or unpauses the game"; }
	@Override public String getUsage() { return "ex: pause"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) termIn.error(ERROR_NO_ARGS);
		
		if (QoT.isPaused()) {
			QoT.unpause();
			termIn.writeln("Game resumed", EColors.yellow);
		}
		else {
			QoT.pause();
			termIn.writeln("Game Paused", EColors.yellow);
		}
	}
	
}

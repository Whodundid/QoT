package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.ConfirmationCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

public class Shutdown extends ConfirmationCommand {
	
	public Shutdown() {
		setCategory("System");
		setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "shutdown"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Closes Minecraft"; }
	@Override public String getUsage() { return "ex: shutdown"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
	}

	@Override
	public void runAction(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		QoT.stopGame();
	}
	
}
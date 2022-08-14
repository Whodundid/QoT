package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.ConfirmationCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import main.QoT;

public class Shutdown extends ConfirmationCommand {
	
	public Shutdown() {
		super(CommandType.NORMAL);
		setCategory("System");
		setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
		numArgs = 0;
	}
	
	@Override public String getName() { return "shutdown"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Closes Minecraft"; }
	@Override public String getUsage() { return "ex: shutdown"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
	}

	@Override
	public void runAction(ETerminal termIn, EList<String> args, boolean runVisually) {
		QoT.stopGame();
	}
	
}
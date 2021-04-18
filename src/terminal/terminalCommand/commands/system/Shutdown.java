package terminal.terminalCommand.commands.system;

import main.Game;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.ConfirmationCommand;
import terminal.window.ETerminal;

public class Shutdown extends ConfirmationCommand {
	
	public Shutdown() {
		super(CommandType.NORMAL);
		setCategory("System");
		setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
		numArgs = 0;
	}
	
	@Override public String getName() { return "shutdown"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>(); }
	@Override public String getHelpInfo(boolean runVisually) { return "Closes Minecraft"; }
	@Override public String getUsage() { return "ex: shutdown"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<String>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { checkConfirm(termIn, args, runVisually); }
	}

	@Override
	public void runAction(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		Game.stopGame();
	}
	
}
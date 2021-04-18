package terminal.terminalCommand.commands.system;

import gameScreens.MainMenuScreen;
import main.Game;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import windowLib.windowObjects.windows.GLKeyChecker;

//Author: Hunter Bragg

public class OpenWindow extends TerminalCommand {
	
	public OpenWindow() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = -1;
	}
	
	@Override public String getName() { return "openscreen"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("screen", "os"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening screens."; }
	@Override public String getUsage() { return "ex: os main"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			switch (args.get(0).toLowerCase()) {
			case "main": Game.displayScreen(new MainMenuScreen()); break;
			case "keys": termIn.getTopParent().displayWindow(new GLKeyChecker()); break;
			default: termIn.error("Unrecognized screen name!");
			}
		}
		else {
			termIn.error("Command input cannot be empty!");
			termIn.info(getUsage());
		}
	}
	
}

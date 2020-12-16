package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.eWindow.windowObjects.windows.GLKeyChecker;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import gameScreens.MainMenuScreen;
import main.Game;
import util.storageUtil.EArrayList;

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

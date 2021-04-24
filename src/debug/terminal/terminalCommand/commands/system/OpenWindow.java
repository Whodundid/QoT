package debug.terminal.terminalCommand.commands.system;

import assets.screens.types.MainMenuScreen;
import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import main.QoT;
import storageUtil.EArrayList;
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
			case "main": QoT.displayScreen(new MainMenuScreen()); break;
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

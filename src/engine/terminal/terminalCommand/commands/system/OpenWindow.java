package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.windowLib.bundledWindows.GLKeyChecker;
import engine.windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

public class OpenWindow extends TerminalCommand {
	
	public OpenWindow() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = -1;
	}
	
	@Override public String getName() { return "openwindow"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("ow"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening windows."; }
	@Override public String getUsage() { return "ex: ow keys"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			switch (args.get(0).toLowerCase()) {
			case "keys": termIn.getTopParent().displayWindow(new GLKeyChecker()); break;
			case "color":
			case "colors": termIn.getTopParent().displayWindow(new ColorPickerSimple(termIn)); break;
			default: termIn.error("Unrecognized screen name!");
			}
		}
		else {
			termIn.error("Command input cannot be empty!");
			termIn.info(getUsage());
		}
	}
	
}

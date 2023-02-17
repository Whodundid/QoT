package envision.debug.terminal.commands.categories.windows;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.GLKeyChecker;
import envision.engine.windows.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_OpenWindow extends TerminalCommand {
	
	public CMD_OpenWindow() {
		setCategory("Windows");
		expectedArgLength = -1;
	}
	
	@Override public String getName() { return "openwindow"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ow"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening windows."; }
	@Override public String getUsage() { return "ex: ow keys"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
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

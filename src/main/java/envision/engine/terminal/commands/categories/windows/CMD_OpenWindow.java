package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
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
	public void runCommand() {
	    expectAtLeast(1);
	    
	    for (String arg : args()) {
	        switch (arg.toLowerCase()) {
	        case "keys": displayWindow(new GLKeyChecker()); break;
	        case "color":
	        case "colors": displayWindow(new ColorPickerSimple()); break;
	        default: error("Unrecognized screen name!");
	        }
	    }
	}
	
}

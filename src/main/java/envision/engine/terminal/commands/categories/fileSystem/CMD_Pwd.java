package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_Pwd extends AbstractFileCommand {
	
	public CMD_Pwd() {
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "pwd"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("dir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current working directory."; }
	@Override public String getUsage() { return "ex: pwd"; }
	@Override public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {}
	
	@Override
	public void runCommand() {
	    expectNoArgs();
	    
	    File path = dirFull();
        String colorPath = "" + EColors.mc_aqua + path;
        writeLink("Current Dir: " + colorPath, String.valueOf(path), path, false, EColors.yellow);
	}
	
}


package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.Envision;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import eutil.colors.EColors;

public class CMD_MkDir extends AbstractFileCommand {
	
	public CMD_MkDir() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "mkdir"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to create a directory."; }
	@Override public String getUsage() { return "ex: mkdir 'name'"; }
	
	@Override
	public Object runCommand() {
	    expectAtLeast(1);
	    
	    for (String input : args()) {
	        File f = parseFilePath(input);
	        
	        if (f.exists() && f.isDirectory()) {
	            error("That directory already exists!");
	            continue;
	        }
	        
	        if (!f.mkdirs()) {
	            error("Failed to create directory!");
	            continue;
	        }
	        
	        String path = f.getName();
	        String colorPath = "" + EColors.mc_aqua + path;
	        writeLink("Created Dir: " + colorPath, path, f, false, EColors.yellow);
	    }
	    
	    Envision.getActiveTopParent().reloadAllWindowInstances(FileExplorerWindow.class);
	    return null;
	}
	
}

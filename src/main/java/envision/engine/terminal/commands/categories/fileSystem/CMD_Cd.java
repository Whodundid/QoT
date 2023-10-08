package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import eutil.colors.EColors;

public class CMD_Cd extends AbstractFileCommand {
	
	public CMD_Cd() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "cd"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the current working directory."; }
	@Override public String getUsage() { return "ex: cd 'dir'"; }
	
	@Override
	public Object runCommand() throws Exception {
	    expectAtLeast(1);
	    
	    // build from relative dir
	    File f = parseFilePath(dir(), firstArg());
	    // if relative didn't exist, try using full path
	    if (!f.exists()) f = new File(firstArg());
        
        setDirectory(f);
        return null;
	}
	
	private void setDirectory(File dir) throws Exception {
	    expectFileNotNull(dir);
	    expectFileExists(dir);
	    expectDirectory(dir);
		
		setDir(dir.getCanonicalFile());
        String path = dir.getName();
        String colorPath = EColors.mc_aqua + path;
        writeLink("Current Dir: " + colorPath, path, dir(), false, EColors.yellow);
	}
	
}

package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import envision.engine.terminal.terminalUtil.TerminalCommandError;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import eutil.colors.EColors;

public class CMD_Rm extends AbstractFileCommand {
	
	public CMD_Rm() {
		expectedArgLength = 1;
		setAcceptedModifiers("-r");
	}
	
	@Override public String getName() { return "rm"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Deletes a file from the file system."; }
	@Override public String getUsage() { return "ex: rm 'dir'"; }
	
	@Override
	public Object runCommand() throws IOException {
	    expectAtLeast(1);
	    
	    boolean recursive = hasModifier("-r");
	    
	    for (String toDelete : args()) {
	        try {
	            File f = parseFilePath(toDelete);
	            deleteFile(f, recursive);
	        }
	        catch (TerminalCommandError e) {
	            error(e.getMessage());
	        }
	        catch (Exception e) {
	            error(e);
	        }
	    }
	    
	    DeveloperDesktop.reloadFileExplorers();
	    return null;
	}
	
	private void deleteFile(File file, boolean recursive) throws TerminalCommandError, IOException {
        expectFileNotNull(file);
        expectFileExists(file);
        
        if (file.isDirectory()) {
            if (recursive) FileUtils.deleteDirectory(file);
            else if (file.list().length > 0) error("'", file.getName(), "' is not empty!");
            else if (!file.delete()) error("Could not delete directory: '", file.getName(), "'");
            else writeln(EColors.yellow, "Deleting directory: ", EColors.mc_aqua, file.getName());
            return;
        }
        
        if (!file.delete()) error("Could not delete file: '", file.getName(), "'");
        else writeln(EColors.yellow, "Deleting file: ", EColors.mc_aqua, file.getName());
	}
	
}

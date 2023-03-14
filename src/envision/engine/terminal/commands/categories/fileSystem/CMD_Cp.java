package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import org.apache.commons.io.FileUtils;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;

public class CMD_Cp extends AbstractFileCommand {
	
	public CMD_Cp() {
		expectedArgLength = 2;
	}
	
	@Override public String getName() { return "cp"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Copies a file from one place and pastes in another."; }
	@Override public String getUsage() { return "ex: cp 'src' 'dest'"; }
	
	@Override
	protected void runCommand() throws Exception {
		expectExactly(2);
		
		File f = fromRelative();
		if (!f.exists()) f = fromFull();
		if (!f.exists()) {
			error("Error: Cannot find the object specified!");
			return;
		}
		
		boolean isDir = f.isDirectory();
		
		File dest = toRelative();
		if (!dest.exists()) dest = toFull();
		if (!dest.exists()) {
			dest = new File(dest, f.getName());
		}
		
		if (isDir) FileUtils.copyDirectory(f, dest);
		else FileUtils.copyFileToDirectory(f, dest);
		
		writeln("Copied object to: " + EColors.white + dest, EColors.green);
	}
	
	private void move(ETerminalWindow termIn, File src, File dest) {
		try {
			if (src.isDirectory()) { FileUtils.copyDirectory(src, dest); }
			else { FileUtils.copyFileToDirectory(src, dest); }
			
			termIn.writeln("Copied object to: " + EColors.white + dest, EColors.green);
		}
		catch (Exception e) {
			termIn.error("Error: Failed to copy object! " + EColors.white + e.toString());
			error(termIn, e);
		}
	}
	
}

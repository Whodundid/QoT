package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import qot.settings.QoTSettings;

public class CMD_Mv extends AbstractFileCommand {
	
	public CMD_Mv() {
		expectedArgLength = 2;
	}
	
	@Override public String getName() { return "mv"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Moves the contents of one file to another."; }
	@Override public String getUsage() { return "ex: mv 'src' 'dest'"; }
	
	@Override
	protected void runCommand() throws Exception {
		expectExactly(2);
		
		boolean isFolder = false;
		File f = fromRelative();
		
		// try full path if relative didn't work
		if (!f.exists()) f = fromFull();
		if (!f.exists()) {
			error("Error: Cannot find the object specified!");
			return;
		}
		
		//check if the file being moved is a directory
		isFolder = f.isDirectory();
		
		if (arg(1).equals("wdir")) {
			move(term, f, new File(QoTSettings.getEditorWorldsDir(), f.getName()));
			return;
		}
		
		//check relative path for destination			
		File dest = toRelative();
		
		if (isFolder) {
			if (!dest.exists()) dest = toFull();
			//if full path still doesn't exist, fail
			if (!dest.exists()) {
				error("Error: Destination path: '" + arg(1) + "' cannot be found!");
				return;
			}
		}
		else if (!dest.exists()) {
			dest = toFull();
			
			//if full path still doesn't exist, assume a rename is happening
			if (!dest.exists()) {
				dest = toRelative();
			}
		}
		// check if potentially moving a file into a directory
		else if (dest.isDirectory()) {
			dest = new File(dest, f.getName());
		}
		
		move(term, f, dest);
	}
	
	private void move(ETerminalWindow termIn, File src, File dest) {
		try {
			var from = Paths.get(src.getCanonicalPath());
			var to = Paths.get(dest.getCanonicalPath());
//			System.out.println("Moving: " + from);
//			System.out.println("To: " + to);
			Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
			termIn.writeln("Moved object to: " + EColors.white + to, EColors.green);
		}
		catch (Exception e) {
			termIn.error("Error: Failed to move object! " + EColors.white + e.toString());
			error(termIn, e);
		}
	}
	
}

package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_RmDir extends AbstractFileCommand {
	
	public CMD_RmDir() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "rmdir"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to delete a directory."; }
	@Override public String getUsage() { return "ex: rmdir 'dir'"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				File homeFile = new File(System.getProperty("user.dir"));
				String homePath = homeFile.getAbsolutePath();
				
				String all = EStringUtil.combineAll(args, " ");
				boolean found = false;
				boolean moreThan = false;
				
				for (int i = 0; i < all.length(); i++) {
					if (all.charAt(i) == '~') {
						if (i == 0) {
							if (i + 1 < all.length() - 1) {
								if (all.charAt(i + 1) == '/' || all.charAt(i + 1) == '\\') {
									if (!found) { found = true; }
									else { moreThan = true; break; }
								}
							}
						}
						else if (i == all.length() - 1) {
							if (i - 1 >= 0) {
								if (all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\') {
									if (!found) { found = true; }
									else { moreThan = true; break; }
								}
							}
						}
						else {
							if ((all.charAt(i + 1) == '/' || all.charAt(i + 1) == '\\') && (all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\')) {
								if (!found) { found = true; }
								else { moreThan = true; break; }
							}
						}
					}
				}
				
				if (moreThan) {
					termIn.error("'" + all + "' is not a valid directory!");
					return;
				}
				
				File f = new File(termIn.getDir(), all);
				
				if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
				if (all.equals("~") && !moreThan) { f = homeFile; }
				else if (all.startsWith("~")) { f = new File(homePath + all.substring(1)); }
				
				//System.out.println("the file: " + f);
				
				if (f.exists()) {
					if (!f.isDirectory()) { termIn.error("Error: specified path is not a directory!"); }
					else {
						if (f.listFiles().length > 0) { termIn.error("Error: directory is not empty!"); }
						else if (!f.delete()) { termIn.error("Error: cannot fully delete specified directory"); }
						else {
							String path = f.getCanonicalPath();
							String colorPath = "" + EColors.mc_aqua + path;
							termIn.writeln("Removing Dir: " + colorPath, EColors.yellow);
						}
					}
				}
				else {
					termIn.error("Error: Cannot find a directory with that name!");
				}
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else {
			termIn.error("Too many arguments!");
		}
	}
	
}
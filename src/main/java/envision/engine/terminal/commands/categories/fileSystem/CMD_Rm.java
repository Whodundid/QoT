package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_Rm extends AbstractFileCommand {
	
	public CMD_Rm() {
		expectedArgLength = 1;
		setAcceptedModifiers("-r");
	}
	
	@Override public String getName() { return "rm"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Deletes a file from the file system."; }
	@Override public String getUsage() { return "ex: rm 'dir'"; }
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (!f.isDirectory()) {
						if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
						termIn.info("Removing file: " + EColors.mc_aqua + f.getName());
					}
					else {
						termIn.error(f.getName() + " is a directory!");
					}
				}
				else {
					termIn.error("Error: Cannot find a file with that path!");
				}
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else if (args.size() == 2) {
			try {
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (f.isDirectory()) {
						if (args.get(1).equals("-r")) {
							if (!deleteRecursively(termIn, f)) { termIn.error("Error: cannot fully delete specified directory"); }
							termIn.info("Removing Dir: " + EColors.mc_aqua + f.getPath());
						}
						else {
							termIn.error("Invalid parameter type: '" + args.get(0) + "'");
						}
					}
					else {
						if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
						termIn.info("Removing file: " + EColors.mc_aqua + f.getName());
					}
				}
				else {
					termIn.error("Error: Cannot find a file with that path!");
				}
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else {
			String all = EStringUtil.combineAll(args.subList(1, args.size() - 1), " ");
			File f = new File(all);
			
			if (f.exists()) {
				if (f.isDirectory()) {
					if (args.get(1).equals("-r")) {
						if (!deleteRecursively(termIn, f)) { termIn.error("Error: cannot fully delete specified directory"); }
						termIn.info("Removing Dir: " + EColors.mc_aqua + f.getPath());
					}
					else {
						termIn.error("Invalid parameter type: '" + args.get(0) + "'");
					}
				}
				else {
					if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
					termIn.info("Removing file: " + EColors.mc_aqua + f.getName());
				}
			}
			else {
				try {
					f = new File(termIn.getDir().getCanonicalPath() + "/" + all);
					
					if (f.exists()) {
						if (f.isDirectory()) {
							if (args.get(1).equals("-r")) {
								if (!deleteRecursively(termIn, f)) { termIn.error("Error: cannot fully delete specified directory"); }
								termIn.info("Removing Dir: " + EColors.mc_aqua + f.getPath());
							}
							else {
								termIn.error("Invalid parameter type: '" + args.get(0) + "'");
							}
						}
						else {
							if (!f.delete()) { termIn.error("Error: cannot delete the specified file"); }
							termIn.info("Removing file: " + EColors.mc_aqua + f.getName());
						}
					}
					else {
						termIn.error("Error: Cannot find a file with that path!");
					}
				}
				catch (Exception e) {
					error(termIn, e);
				}
			} //else
		}
	}
	
}

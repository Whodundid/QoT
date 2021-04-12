package envisionEngine.terminal.terminalCommand.commands.fileSystem;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.window.ETerminal;
import eutil.EUtil;
import java.io.File;
import renderUtil.EColors;
import storageUtil.EArrayList;

public class Rm extends FileCommand {
	
	public Rm() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "rm"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Deletes a file from the file system."; }
	@Override public String getUsage() { return "ex: rm 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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
			String all = EUtil.combineAll(args.subList(1, args.size() - 1), " ");
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

package terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import renderUtil.EColors;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.window.ETerminal;

public class MkDir extends FileCommand {
	
	public MkDir() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "mkdir"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Attempts to create a directory."; }
	@Override public String getUsage() { return "ex: mkdir 'name'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() == 1) {
			try {
				
				File f = new File(termIn.getDir().getCanonicalPath() + "/" + args.get(0));
				
				if (f.exists()) {
					if (!f.isDirectory()) {
						if (f.mkdirs()) {
							String path = f.getPath();
							String colorPath = "" + EColors.mc_aqua + path;
							termIn.writeLink("Created Dir: " + colorPath, path, new File(path), false, EColors.yellow);
						}
					}
					else { termIn.error("That directory already exists!"); }
				}
				else {
					if (f.mkdirs()) {
						String path = f.getPath();
						String colorPath = "" + EColors.mc_aqua + path;
						termIn.writeLink("Created Dir: " + colorPath, path, new File(path), false, EColors.yellow);
					}
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

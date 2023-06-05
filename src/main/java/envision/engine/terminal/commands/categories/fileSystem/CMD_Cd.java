package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_Cd extends AbstractFileCommand {
	
	public CMD_Cd() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "cd"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the current working directory."; }
	@Override public String getUsage() { return "ex: cd 'dir'"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() >= 1) {
			try {
				String all = EStringUtil.combineAll(args, " ");
				File f = parsePath(termIn, args);
				
				if (f.exists()) setDir(termIn, args, runVisually, f);
				else {
					f = new File(all);
					
					if (f.exists()) setDir(termIn, args, runVisually, f);
					else {
						if (args.get(0).startsWith("..")) f = new File(termIn.getDir(), args.get(0));
						else f = new File(args.get(0));
						
						if (f.exists()) setDir(termIn, args, runVisually, f);
						else {
							f = new File(termIn.getDir(), args.get(0));
							
							if (f.exists()) setDir(termIn, args, runVisually, f);
							else {
								termIn.error("'" + args.get(0) + "' is not a vaild directory!");
							}
						}
					}
				}
				
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
	}
	
	private void setDir(ETerminalWindow termIn, EList<String> args, boolean runVisually, File dir) {
		if (dir == null) { dir = termIn.getDir(); }
		
		if (dir.exists()) {
			if (dir.isDirectory()) {
				try {
					termIn.setDir(new File(dir.getCanonicalPath()));
					String path = dir.getCanonicalPath();
					String colorPath = "" + EColors.mc_aqua + path;
					termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
				}
				catch (IOException e) {
					error(termIn, e);
				}
			}
			else { termIn.error(dir.getName() + " is a file not a directory!"); }
		}
		else { termIn.error("'" + dir.getAbsolutePath() + "' is not a vaild directory!"); }
	}
	
}

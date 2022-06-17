package engine.terminal.terminalCommand.commands.fileSystem;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;
import java.io.File;
import java.io.IOException;

public class Cd extends FileCommand {
	
	public Cd() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "cd"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the current working directory."; }
	@Override public String getUsage() { return "ex: cd 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		if (args.size() >= 1) {
			try {
				String all = StringUtil.combineAll(args, " ");
				File f = parsePath(termIn, args);
				
				System.out.println(f + " : " + args);
				
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
	
	private void setDir(ETerminal termIn, EArrayList<String> args, boolean runVisually, File dir) {
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
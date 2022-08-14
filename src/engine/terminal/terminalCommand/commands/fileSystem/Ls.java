package engine.terminal.terminalCommand.commands.fileSystem;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;
import java.io.File;
import java.io.IOException;

public class Ls extends FileCommand {
	
	public Ls() {
		super(CommandType.NORMAL);
		setAcceptedModifiers("-a");
		numArgs = 1;
	}
	
	@Override public String getName() { return "ls"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "List all files in a directory."; }
	@Override public String getUsage() { return "ex: ls 'dir'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { listDir(termIn, false, null); }
		else if (args.size() >= 1) {
			File theFile = null;
			boolean hidden = hasModifier("-a");
			
			String all = StringUtil.combineAll(args, " ");
			EArrayList<String> allArgs = new EArrayList(args);
			allArgs.add(all);
			
			for (String s : allArgs) {
				theFile = new File(s);
				if (!theFile.exists()) {
					theFile = new File(termIn.getDir(), s);
				}
			}
			
			if (args.get(0).equals("~")) theFile = new File(System.getProperty("user.dir"));
			
			listDir(termIn, hidden, theFile);
		}
		else if (args.size() > modifiers.size() + 1) {
			termIn.error("Too many arguments!");
		}
	}
	
	private void listDir(ETerminal termIn, boolean showHidden, File dir) {
		if (dir == null) { dir = termIn.getDir(); }
		
		if (dir.exists()) {
			
			if (!dir.isDirectory()) { termIn.error(dir.getName() + " is a file, not a directory!"); return; }
			
			try {
				String path = dir.getCanonicalPath();
				String colorPath = "" + EColors.mc_aqua + path;
				termIn.writeLink("Viewing Dir: " + colorPath, path, new File(path), false, EColors.yellow);
			}
			catch (IOException e) {
				error(termIn, e);
			}
			
			if (dir.list().length > 0) { termIn.writeln(); }
			
			for (String s : dir.list()) {
				File f = new File(dir, s);
				
				//System.out.printf("%-50s%-100s%s\n", s, f, f.isDirectory());
				
				if (f.isHidden() && !showHidden) { continue; }
				
				if (f.isDirectory()) { termIn.writeln(f.getName() + "/", 0xff2265f0); }
				else { termIn.writeln(s, EColors.green); }
			}
		}
		else {
			try {
				termIn.error("'" + dir.getCanonicalPath() + "' is not a vaild directory!");
			}
			catch (IOException e) {
				error(termIn, e);
			}
		}
	}
	
}

package envisionEngine.terminal.terminalCommand.commands.fileSystem;

import java.io.File;

import envisionEngine.terminal.window.ETerminal;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.strings.EStringUtil;
import eutil.sys.OSType;

public class Ls extends FileCommand {
	
	public Ls() {
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "ls"; }
	@Override public String getHelpInfo(boolean runVisually) { return "List all files in a directory."; }
	@Override public String getUsage() { return "ex: ls 'dir'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		//if empty -- ls current dir
		if (args.isEmpty()) {
			listDir(termIn, null);
			return;
		}
		
		//check if too many args
		if (args.size() > 1) {
			errorUsage(termIn, ERROR_TOO_MANY);
			return;
		}
		
		File theFile = null;
		
		String all = EStringUtil.combineAll(args, " ");
		EArrayList<String> allArgs = new EArrayList<>(args);
		allArgs.add(all);
		
		for (String s : allArgs) {
			theFile = new File(s);
			if (!theFile.exists()) {
				theFile = new File(termIn.getDir(), s);
			}
		}
		
		if (args.get(0).equals("~")) {
			theFile = EFileUtil.userDir();
		}
		
		listDir(termIn, theFile);
	}
	
	private void listDir(ETerminal termIn, File dir) {
		if (dir == null) dir = termIn.getDir();
		
		//check if actually exists
		if (!EUtil.fileExists(dir)) {
			termIn.error("'" + dir.getAbsolutePath() + "' is not a vaild directory!");
			return;
		}
		
		//check if actually a directory
		if (!dir.isDirectory()) {
			termIn.error(dir.getName() + " is a file, not a directory!");
			return;
		}
		
		try {
			String path = dir.getAbsolutePath();
			String colorPath = EColors.mc_aqua + path;
			boolean showHidden = hasModifier("-a");
			
			termIn.writeLink("Viewing Dir: " + colorPath, path, new File(path), false, EColors.yellow);
			if (dir.list().length > 0) termIn.writeln();
			
			if (showHidden && OSType.isWindows()) {
				termIn.writeln(".", 0xff2265f0);
				termIn.writeln("..", 0xff2265f0);
			}
			
			for (File f : dir.listFiles()) {
				//only show hidden files if '-a'
				if (f.isHidden() && !showHidden) continue;
				termIn.writeln(f.getName(), (f.isDirectory()) ? 0xff2265f0 : EColors.green.intVal);
			}
		}
		catch (Exception e) {
			error(termIn, e);
		}
	}
	
}

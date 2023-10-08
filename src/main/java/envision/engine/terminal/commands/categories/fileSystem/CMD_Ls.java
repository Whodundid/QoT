package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;

import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.strings.EStringUtil;
import eutil.sys.OSType;
import qot.settings.QoTSettings;

public class CMD_Ls extends AbstractFileCommand {
	
	public CMD_Ls() {
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "ls"; }
	@Override public String getHelpInfo(boolean runVisually) { return "List all files in a directory."; }
	@Override public String getUsage() { return "ex: ls 'dir'"; }
	
	@Override
	public Object runCommand() {
		//if empty -- ls current dir
		if (args().isEmpty()) {
			listDir(null);
			return null;
		}
		
		//check if too many args
		if (argLength() > 1) {
			errorUsage(term, ERROR_TOO_MANY);
			return null;
		}
		
		File theFile = null;
		
		theFile = switch (firstArg()) {
		case "." -> term.getDir();
		case ".." -> term.getDir().getParentFile();
		case "~" -> USER_DIR;
		case "wdir" -> QoTSettings.getEditorWorldsDir();
		default -> null;
		};
		
		if (theFile == null) {
			String all = EStringUtil.combineAll(args(), " ");
			EList<String> allArgs = new EArrayList<>(args());
			allArgs.add(all);
			
			for (String s : allArgs) {
				theFile = new File(s);
				if (!theFile.exists()) {
					theFile = new File(dir(), s);
				}
			}
			
			if (firstArg().equals("~")) {
				theFile = EFileUtil.userDir();
			}
		}
		
		listDir(theFile);
		return null;
	}
	
	private void listDir(File dir) {
		if (dir == null) dir = dir();
		
		//check if actually exists
		if (!EUtil.fileExists(dir)) {
			error("'" + dir.getAbsolutePath() + "' is not a vaild directory!");
			return;
		}
		
		//check if actually a directory
		if (!dir.isDirectory()) {
			error(dir.getName() + " is a file, not a directory!");
			return;
		}
		
		try {
			String colorPath = EColors.mc_aqua + dir.getName();
			boolean showHidden = hasModifier("-a");
			
			writeLink("Viewing Dir: " + colorPath, dir.getName(), dir, false, EColors.yellow);
			if (dir.list().length > 0) writeln();
			
			if (showHidden && OSType.isWindows()) {
				writeln(".", 0xff2265f0);
				writeln("..", 0xff2265f0);
			}
			
			for (File f : dir.listFiles()) {
				//only show hidden files if '-a'
				if (f.isHidden() && !showHidden) continue;
				
				var name = ((f.isDirectory()) ? EColors.blue : EColors.green) + f.getName();
				
				writeln(name);
			}
		}
		catch (Exception e) {
			error(e);
		}
	}
	
}

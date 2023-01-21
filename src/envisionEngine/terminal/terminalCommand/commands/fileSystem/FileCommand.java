package envisionEngine.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.io.IOException;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public abstract class FileCommand extends TerminalCommand {
	
	//--------------
	// Constructors
	//--------------
	
	protected FileCommand() {
		setCategory("File System");
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void handleTabComplete(ETerminal termIn, EList<String> args) {
		fileTabComplete(termIn, args);
	}
	
	//---------------------
	// FileCommand Methods
	//---------------------
	
	protected void fileTabComplete(ETerminal termIn, EList<String> args) {
		try {
			fileTabComplete(termIn, termIn.getDir().getCanonicalPath(), args);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void fileTabComplete(ETerminal termIn, File dir, EList<String> args) {
		try {
			fileTabComplete(termIn, dir.getCanonicalPath(), args);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void fileTabComplete(ETerminal termIn, String dir, EList<String> args) {
		try {
			if (args.isEmpty()) {
				File f = new File(dir);
				
				EList<String> options = EList.newList();
				
				for (File file : f.listFiles()) {
					options.add(file.getName());
				}
				
				termIn.buildTabCompletions(options);
				if (!termIn.getTab1()) termIn.setTextTabBeing("");
			}
			else if (!termIn.getTab1()) {
				EList<String> options = EList.newList();
				
				File home = new File(System.getProperty("user.dir"));
				File f = new File(dir);
				String all = EStringUtil.combineAll(args, " ").replace("/", "\\");
				boolean isHome = all.startsWith("~");
				
				
				if (!all.endsWith("/") && !all.endsWith("\\")) { //search for name complete
					
					File testPath = new File(args.get(termIn.getCurrentArg() - 1));
					String search = "";
					
					if (testPath.exists()) {
						f = testPath;
						termIn.setTextTabBeing(args.get(0));
					}
					else {
						int pos = EStringUtil.findStartingIndex(all, "\\", true);
						if (pos >= 0) {
							String path = all.substring(isHome ? 1 : 0, pos + 1);
							termIn.setTabBase(EStringUtil.subStringToSpace(path, 0, true));
							f = new File(path);
							if (!f.exists()) { f = new File(termIn.getDir(), path); }
							termIn.setTextTabBeing(path);
							termIn.setTab1(false);
						}
					}
					
					search = !termIn.getTab1() ? EStringUtil.subStringToString(all, 0, "\\", true) : termIn.getTextTabBegin();
					
					if (f != null && f.listFiles() != null) {
						for (File file : f.listFiles()) {
							String fName = file.getName().toLowerCase();
							String check = search.toLowerCase();
							
							if (fName.startsWith(check)) {
								options.add(file.getName());
							}
						}
					}
				}
				else { //search for directory complete
					boolean isDrive = false;
					for (File driveLetter : File.listRoots()) {
						if (all.toLowerCase().equals(driveLetter.toString().toLowerCase())) { isDrive = true; break; }
					}
					
					all = all.substring(isHome ? 1 : 0, isDrive ? all.length() : all.length() - 1);
					
					if (isHome) { f = new File(home, all); }
					else { f = new File(all); }
					
					if (!f.exists()) { f = new File(termIn.getDir(), all); }
					termIn.setTabBase(EStringUtil.subStringToSpace(all, 0, true) + (isDrive ? "" : "\\"));
					
					if (f != null && f.listFiles() != null) {
						for (File file : f.listFiles()) {
							options.add(file.getName());
						}
					}
				}
				
				termIn.buildTabCompletions(options);
				if (!termIn.getTab1()) termIn.setTextTabBeing(args.get(0));
			}
		}
		catch (Exception e) {
			error(termIn, e);
		}
	}
	
	protected boolean deleteRecursively(ETerminal termIn, File path) {
		File[] allContents = path.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteRecursively(termIn, file);
	        }
	    }
	    return path.delete();
	}
	
	protected File parsePath(ETerminal termIn, EList<String> args) {
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
							if (!found) found = true;
							else moreThan = true; break;
						}
					}
				}
				else if (i == all.length() - 1) {
					if (i - 1 >= 0) {
						if (all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\') {
							if (!found) found = true;
							else moreThan = true; break;
						}
					}
				}
				else {
					if ((all.charAt(i + 1) == '/' || all.charAt(i + 1) == '\\') && (all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\')) {
						if (!found) found = true;
						else moreThan = true; break;
					}
				}
			}
		}
		
		if (moreThan) {
			termIn.error("'" + all + "' is not a valid directory!");
			return null;
		}
		
		File f = new File(termIn.getDir(), all);
		
		if (all.startsWith("..")) f = new File(termIn.getDir(), args.get(0));
		if (all.equals("~") && !moreThan) f = homeFile;
		else if (all.startsWith("~")) f = new File(homePath + all.substring(1));
		
		return f;
	}

}

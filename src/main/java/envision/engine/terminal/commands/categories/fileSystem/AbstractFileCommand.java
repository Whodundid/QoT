package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.terminalUtil.FileHelper;
import envision.engine.terminal.terminalUtil.TermArgLengthException;
import envision.engine.terminal.terminalUtil.TermArgParsingException;
import envision.engine.terminal.terminalUtil.TerminalCommandError;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public abstract class AbstractFileCommand extends TerminalCommand {
	
    public static final String ERROR_FILE_NULL = "The given file path is null!";
    public static final String ERROR_NOT_EXISTS = "File: '%s' does not exist!";
    public static final String ERROR_NOT_DIRECTORY = "File: '%s' is a file not a directory!";
    public static final String ERROR_NOT_FILE = "File: '%s' is directory not a a file!";
    
	protected FileHelper fileHelper;
	
	//==============
	// Constructors
	//==============
	
	protected AbstractFileCommand() {
		setCategory("File System");
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		fileTabComplete(termIn, args);
	}
	
    @Override
    public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
        try {
            term = termIn;
            argHelper = fileHelper = new FileHelper(termIn, args, runVisually);
            runCommand();
        }
        catch (TermArgParsingException e) {
            e.display(termIn);
        }
        catch (TermArgLengthException e) {
            errorUsage(e.getMessage());
        }
        catch (Exception e) {
            error(e);
        }
    }
	
	//=========
	// Methods
	//=========
	
	protected File fromRelative() { return fileHelper.fromRelative(); }
	protected File fromFull() { return fileHelper.fromFull(); }
	protected File toRelative() { return fileHelper.toRelative(); }
	protected File toFull() { return fileHelper.toFull(); }
	
	protected String fromName() { return fileHelper.fromName(); }
	protected String toName() { return fileHelper.toName(); }
	
	//===============================
	// Common File Errors Assertions
	//===============================
	
	/** Throws a default error if the given file path was null. */
    protected void expectFileNotNull(File file) { expectFileNotNull(file, ERROR_FILE_NULL); }
    /** Throws a customized error if the given file path was null. */
    protected void expectFileNotNull(File file, String message) {
        if (file != null) return;
        if (message != null) throw new TerminalCommandError(message);
        throw new TerminalCommandError(ERROR_FILE_NULL);
    }
    
    /** Throws a default error if the given file path does not exist. */
    protected void expectFileExists(File file) { expectFileExists(file, null); }
    /** Throws a customized error if the given file path does not exist. */
    protected void expectFileExists(File file, String message) {
        if (file.exists()) return;
        if (message != null) throw new TerminalCommandError(message);
        throw new TerminalCommandError(String.format(ERROR_NOT_EXISTS, file.getName()));
    }
    
    /** Throws a default error if the given file path was a directory, not a file. */
    protected void expectFile(File file) { expectFile(file, null); }
    /** Throws a customized error if the given file path was a directory, not a file. */
    protected void expectFile(File file, String message) {
        if (!file.isDirectory()) return;
        if (message != null) throw new TerminalCommandError(message);
        throw new TerminalCommandError(String.format(ERROR_NOT_FILE, file.getName()));
    }
    
    /** Throws a default error if the given file path was a file, not a directory. */
    protected void expectDirectory(File file) { expectDirectory(file, null); }
    /** Throws a customized error if the given file path was a file, not a directory. */
    protected void expectDirectory(File file, String message) {
        if (file.isDirectory()) return;
        if (message != null) throw new TerminalCommandError(message);
        throw new TerminalCommandError(String.format(ERROR_NOT_DIRECTORY, file.getName()));
    }
	
    //=====================
    // Useful File Methods
    //=====================
    
	protected void fileTabComplete(ETerminalWindow termIn, EList<String> args) {
		try {
			fileTabComplete(termIn, termIn.getDir().getCanonicalPath(), args);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void fileTabComplete(ETerminalWindow termIn, File dir, EList<String> args) {
		try {
			fileTabComplete(termIn, dir.getCanonicalPath(), args);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void fileTabComplete(ETerminalWindow termIn, String dir, EList<String> args) {
		// the dir to start trying to parse a tab complete from -- can change based on given path
		File tabCompleteDir = new File(dir);
		
		if (args.isEmpty()) {
			EList<String> options = EList.newList();
			
			for (File file : tabCompleteDir.listFiles()) {
				options.add(file.getName());
			}
			
			termIn.buildTabCompletions(options);
			if (!termIn.getTab1()) termIn.setTextTabBeing("");
			return;
		}
		
		// exit out if they've already pressed tab once (we don't need to build new results)
		if (termIn.getTab1()) return;
		
		EList<String> options = EList.newList();
		
		String inputPath = args.getLast().replace("\\", SEPARATOR).replace("/", SEPARATOR);
		boolean isHome = inputPath.startsWith("~" + SEPARATOR);
		
		//search for name complete
		if (!inputPath.endsWith("/") && !inputPath.endsWith("\\")) {
			File testPath = new File(args.get(termIn.getCurrentArg() - 1));
			String search = "";
			
			if (testPath.exists()) {
				tabCompleteDir = testPath;
				termIn.setTextTabBeing(args.get(0));
			}
			else {
				int pos = EStringUtil.findStartingIndex(inputPath, "\\", true);
				
				if (pos >= 0) {
					String path = inputPath.substring(isHome ? 1 : 0, pos + 1);
					termIn.setTabBase(EStringUtil.subStringToSpace(path, 0, true));
					tabCompleteDir = new File(path);
					
					if (!tabCompleteDir.exists()) {
						tabCompleteDir = new File(termIn.getDir(), path);
					}
					
					termIn.setTextTabBeing(path);
					termIn.setTab1(false);
				}
			}
			
			search = !termIn.getTab1() ? EStringUtil.subStringToString(inputPath, 0, "\\", true) : termIn.getTextTabBegin();
			
			for (File file : tabCompleteDir.listFiles()) {
				String fName = file.getName();
				String check = search;
				
				if (fName.startsWith(check)) {
					options.add(file.getName());
				}
			}
		}
		//search for directory complete
		else {
			tabCompleteDir = parseFilePath(termIn.getDir(), inputPath);
			
			String tabBase = EStringUtil.subStringToSpace(inputPath, 0, true);
			termIn.setTabBase(tabBase);
			
			for (File file : tabCompleteDir.listFiles()) {
				options.add(file.getName());
			}
		}
		
		// finally build the tab completions
		termIn.buildTabCompletions(options);
		
		if (!termIn.getTab1()) {
			termIn.setTextTabBeing(args.get(0));
		}
	}
	
	//===============================================================================================
	
	protected File parseFirstArgFilePath() { return parseFilePath(dir(), firstArg()); }
	protected File parseFilePath(final String pathIn) { return parseFilePath(dir(), pathIn); }
	protected File parseFilePath(final File currentDir, final String pathIn) {
		// prevent null paths from being evaluated
		if (pathIn == null) return null;
		// assume empty paths are referring to local dir
		if (pathIn.isBlank()) return currentDir;
		
		// get all separators on the same page
		String workingPath = pathIn.replace("\\", SEPARATOR).replace("/", SEPARATOR);
		
		File dir = currentDir;
		File parentDir = currentDir.getParentFile();
		
		// check for paths that have no parent dir (C:/ ..)
		if (parentDir == null) parentDir = dir;
		
		// try the simple replacement ones first
		switch (workingPath) {
		case ".": return dir;
		case "..": return parentDir;
		case "~": return USER_DIR;
		case "/", "\\": return ROOT_DIR;
		default: break;
		}
		
		String parsedPath = null;
		
		// if dir starts with a '.' replace with 'dir'
		if (workingPath.startsWith("." + SEPARATOR)) parsedPath = dir + workingPath.substring(1);
		// if dir starts with a '..' replace with 'parentDir'
		else if (workingPath.startsWith(".." + SEPARATOR)) parsedPath = parentDir + workingPath.substring(2);
		// replace '~' with starting user dir
		else if (workingPath.startsWith("~" + SEPARATOR)) parsedPath = USER_DIR + workingPath.substring(1);
		
		// check if we found a match
		if (parsedPath != null) {
			return new File(parsedPath);
		}
		
		// check if it could be a drive root
		final String allLower = workingPath.toLowerCase();
		for (File driveLetter : File.listRoots()) {
			final var driveLower = String.valueOf(driveLetter).toLowerCase();
			if (allLower.equals(driveLower)) {
				return driveLetter;
			}
		}
		
		return new File(currentDir, workingPath);
	}
	
	protected boolean deleteRecursively(ETerminalWindow termIn, File path) {
		File[] allContents = path.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteRecursively(termIn, file);
	        }
	    }
	    return path.delete();
	}
	
	protected File parsePath(ETerminalWindow termIn, EList<String> args) {
		File homeFile = new File(System.getProperty("user.dir"));
		String homePath = homeFile.getAbsolutePath();
		
		String all = EStringUtil.combineAll(args, " ");
		boolean found = false;
		boolean moreThan = false;
		
		for (int i = 0; i < all.length(); i++) {
			char c = all.charAt(i);
			if (c != '~') continue;
			if (all.length() == 1) break;
			
			if (i == 0) {
				if (all.charAt(i + 1) == '/' || all.charAt(i + 1) == '\\') {
					if (!found) found = true;
					else moreThan = true; break;
				}
			}
			else if (i == all.length() - 1) {
				if (i - 1 >= 0 && all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\') {
					if (!found) found = true;
					else moreThan = true; break;
				}
			}
			else if ((all.charAt(i + 1) == '/' || all.charAt(i + 1) == '\\') && (all.charAt(i - 1) == '/' || all.charAt(i - 1) == '\\')) {
				if (!found) found = true;
				else moreThan = true; break;
			}
		}
		
		if (moreThan) {
			termIn.error("'" + all + "' is not a valid directory!");
			return null;
		}
		
		File f = new File(termIn.getDir(), all);
		
		if (all.startsWith("..")) 					f = new File(termIn.getDir(), args.get(0));
		else if (all.equals("~") && !moreThan) 		f = homeFile;
		else if (all.startsWith("~")) 				f = new File(homePath + all.substring(1));
		
		return f;
	}

}

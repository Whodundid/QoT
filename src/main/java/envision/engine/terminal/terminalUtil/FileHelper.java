package envision.engine.terminal.terminalUtil;

import java.io.File;
import java.io.IOException;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public class FileHelper extends ArgHelper {
	
	private File fromRelative;
	private File fromFull;
	private File toRelative;
	private File toFull;
	
	public FileHelper(ETerminalWindow termIn, EList<String> argsIn, boolean runVisuallyIn) throws IOException {
		super(termIn, argsIn, runVisuallyIn);
		
		if (size == 0) {
			fromRelative = null;
			fromFull = null;
			toRelative = null;
			toFull = null;
		}
		else if (size == 1) {
		    fromRelative = generateFile(curDir, arg(0));
			fromFull = new File(arg(0)).getCanonicalFile();
			toRelative = null;
			toFull = null;
		}
		else {
			fromRelative = generateFile(curDir, arg(0));
			fromFull = new File(arg(0)).getCanonicalFile();
			toRelative = generateFile(curDir, arg(1)).getCanonicalFile();
			toFull = new File(arg(1)).getCanonicalFile();
		}
	}
	
	private File generateFile(File cur, String path) {
	    try {
	        return new File(curDir, path).getCanonicalFile();
	    }
	    catch (Exception e) {
	        return curDir;
	    }
	}
	
	public File fromRelative() { return fromRelative; }
	public File fromFull() { return fromFull; }
	public File toRelative() { return toRelative; }
	public File toFull() { return toFull; }
	
	public String fromName() { return (fromRelative != null) ? fromRelative.getName() : null; }
	public String toName() { return (toRelative != null) ? toRelative.getName() : null; }
	
}

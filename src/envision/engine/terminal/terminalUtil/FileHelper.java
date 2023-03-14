package envision.engine.terminal.terminalUtil;

import java.io.File;
import java.io.IOException;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public class FileHelper extends ArgHelper {
	
	private final File fromRelative;
	private final File fromFull;
	private final File toRelative;
	private final File toFull;
	
	public FileHelper(ETerminalWindow termIn, EList<String> argsIn, boolean runVisuallyIn) throws IOException {
		super(termIn, argsIn, runVisuallyIn);
	
		if (size == 0) {
			fromRelative = null;
			fromFull = null;
			toRelative = null;
			toFull = null;
		}
		else if (size == 1) {
			fromRelative = new File(curDir, argsIn.get(0)).getCanonicalFile();
			fromFull = new File(argsIn.get(0)).getCanonicalFile();
			toRelative = null;
			toFull = null;
		}
		else {
			fromRelative = new File(curDir, argsIn.get(0)).getCanonicalFile();
			fromFull = new File(argsIn.get(0)).getCanonicalFile();
			toRelative = new File(curDir, argsIn.get(1)).getCanonicalFile();
			toFull = new File(argsIn.get(1)).getCanonicalFile();
		}
	}
	
	public File fromRelative() { return fromRelative; }
	public File fromFull() { return fromFull; }
	public File toRelative() { return toRelative; }
	public File toFull() { return toFull; }
	
	public String fromName() { return (fromRelative != null) ? fromRelative.getName() : null; }
	public String toName() { return (toRelative != null) ? toRelative.getName() : null; }
	
}

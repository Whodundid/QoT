package envision.debug.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class CMD_Edit extends AbstractFileCommand {
	
	public CMD_Edit() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "edit"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to edit the contents of a file."; }
	@Override public String getUsage() { return "ex: edit 'file'"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		else if (args.size() >= 1) {
			try {
				boolean openA = args.getLast().equals("-a");
				String all = "";
				
				if (openA) { all = EStringUtil.combineAll(args.subList(0, args.size() - 1), " "); }
				else { all = EStringUtil.combineAll(args, " "); }
				
				File f = new File(termIn.getDir(), all);
				
				if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
				if (args.get(0).equals("~")) { f = new File(System.getProperty("user.dir")); }
				
				if (f.exists()) { check(termIn, f); }
				else {
					f = new File(all);
					
					if (f.exists()) { check(termIn, f); }
					else {
						if (args.get(0).startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
						else { f = new File(args.get(0)); }
						
						if (f.exists()) { check(termIn, f); }
						else {
							f = new File(termIn.getDir(), args.get(0));
							
							if (f.exists()) { check(termIn, f); }
							else {
								try {
									openEditWindow(termIn, f);
								}
								catch (Exception e) {
									termIn.error("'" + args.get(0) + "' is not a vaild file!");
									error(termIn, e);
								}
							} //else
						}
					} //else
				}
				
			}
			catch (Exception e) { error(termIn, e); }
		}
	}
	
	private void check(ETerminalWindow termIn, File path) {
		if (path.isDirectory()) { termIn.error("Error: " + path.getName() + " is a directory!"); }
		else { openEditWindow(termIn, path); }
	}
	
	private void openEditWindow(ETerminalWindow termIn, File path) {
		if (path != null) {
			termIn.info("Opening edit window..");
			
			TextEditorWindow window = new TextEditorWindow(path);
			window.setFocusedObjectOnClose(termIn);
			
			termIn.getTopParent().displayWindow(window, ObjectPosition.SCREEN_CENTER);
			
			window.setFocusToLineIfEmpty();
		}
	}

}

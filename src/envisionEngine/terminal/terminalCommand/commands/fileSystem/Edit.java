package envisionEngine.terminal.terminalCommand.commands.fileSystem;

import envisionEngine.eWindow.windowObjects.windows.TextEditorWindow;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.window.ETerminal;
import java.io.File;
import main.Game;
import util.EUtil;
import util.renderUtil.CenterType;
import util.storageUtil.EArrayList;

public class Edit extends FileCommand {
	
	public Edit() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "edit"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to edit the contents of a file."; }
	@Override public String getUsage() { return "ex: edit 'file'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); }
		else if (args.size() >= 1) {
			try {
				boolean openA = args.getLast().equals("-a");
				String all = "";
				
				if (openA) { all = EUtil.combineAll(args.subList(0, args.size() - 1), " "); }
				else { all = EUtil.combineAll(args, " "); }
				
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
	
	private void check(ETerminal termIn, File path) {
		if (path.isDirectory()) { termIn.error("Error: " + path.getName() + " is a directory!"); }
		else { openEditWindow(termIn, path); }
	}
	
	private void openEditWindow(ETerminal termIn, File path) {
		if (path != null) {
			termIn.info("Opening edit window..");
			
			TextEditorWindow window = new TextEditorWindow(path);
			window.setFocusedObjectOnClose(termIn);
			
			Game.displayWindow(window, CenterType.screen);
			
			window.setFocusToLineIfEmpty();
		}
	}

}

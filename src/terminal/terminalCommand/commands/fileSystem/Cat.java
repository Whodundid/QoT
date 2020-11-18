package terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.util.Scanner;
import terminal.terminalCommand.CommandType;
import terminal.window.ETerminal;
import util.EUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class Cat extends FileCommand {
	
	public Cat() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}
	
	@Override public String getName() { return "cat"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the content of a file."; }
	@Override public String getUsage() { return "ex: cat '.txt'"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { fileTabComplete(termIn, args); }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.size() == 0) { termIn.error("Not enough arguments!"); }
			else if (args.size() >= 1) {
				String all = EUtil.combineAll(args, " ");
				File f = new File(all);
				
				if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
				
				if (f.exists()) { displayFile(termIn, f); }
				else {
					f = new File(termIn.getDir(), all);
					
					if (f.exists()) { displayFile(termIn, f); }
					else {
						if (args.get(0).startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
						else { f = new File(args.get(0)); }
						
						if (f.exists()) { displayFile(termIn, f); }
						else {
							f = new File(termIn.getDir(), args.get(0));
							
							if (f.exists()) { displayFile(termIn, f); }
							else {
								termIn.error("'" + args.get(0) + "' is not a vaild directory!");
							}
						}
					}
				}
			}
			else { termIn.error("Too many arguments!"); }
		}
		catch (Exception e) {
			error(termIn, e);
		}
	}
	
	private void displayFile(ETerminal termIn, File fileIn) {
		if (!fileIn.isDirectory()) {
			try (Scanner reader = new Scanner(fileIn)) {
				if (reader.hasNext()) {
					termIn.info("Displaying content:\n");
				}
				while (reader.hasNext()) {
					String s = reader.nextLine();
					termIn.writeln(s, EColors.lgray);
				}
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else { termIn.error(fileIn.getName() + " is a directory not a file!"); }
	}
	
}
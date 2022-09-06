package envision.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.util.Scanner;

import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

public class Head extends FileCommand {
	
	public Head() {
		numArgs = 1;
	}
	
	@Override public String getName() { return "head"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the first few specified lines of a file. By default it displays 10 lines."; }
	@Override public String getUsage() { return "ex: head 'file path' 10"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() == 1) { tryFind(termIn, args, 10); }
		else if (args.size() == 2) {
			try {
				long len = Long.parseLong(args.get(1));
				
				if (len < 0) { termIn.error("Value cannot be negative!"); }
				else if (len > Integer.MAX_VALUE) { termIn.error("Value is too large!"); }
				else {
					tryFind(termIn, args, (int) len);
				}
			}
			catch (NumberFormatException e) { termIn.error("Cannot parse arg!"); }
			catch (Exception e) { error(termIn, e); }
		}
		else { termIn.error("Too many arguments!"); termIn.info(getUsage()); }
	}
	
	private void tryFind(ETerminal termIn, EArrayList<String> args, int len) {
		String all = EStringUtil.combineAll(args, " ");
		File f = new File(all);
		
		if (all.startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
		
		if (f.exists()) { display(termIn, f, len); }
		else {
			f = new File(termIn.getDir(), all);
			
			if (f.exists()) { display(termIn, f, len); }
			else {
				if (args.get(0).startsWith("..")) { f = new File(termIn.getDir(), args.get(0)); }
				else { f = new File(args.get(0)); }
				
				if (f.exists()) { display(termIn, f, len); }
				else {
					f = new File(termIn.getDir(), args.get(0));
					
					if (f.exists()) { display(termIn, f, len); }
					else {
						termIn.error("'" + args.get(0) + "' is not a vaild directory!");
					}
				}
			}
		}
	}
	
	private void display(ETerminal termIn, File fileIn, int len) {
		if (!fileIn.isDirectory()) {
			try (Scanner reader = new Scanner(fileIn)) {
				if (reader.hasNext()) {
					termIn.info("Displaying content:\n");
				}
				
				int i = 0;
				if (len > 0) {
					while (reader.hasNext() && i < len) {
						String s = reader.nextLine();
						termIn.writeln(s, EColors.lgray);
						i++;
					}
				}
			}
			catch (Exception e) { error(termIn, e); }
		}
		else { termIn.error(fileIn.getName() + " is a directory not a file!"); }
	}

}

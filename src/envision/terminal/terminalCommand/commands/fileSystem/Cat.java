package envision.terminal.terminalCommand.commands.fileSystem;

import java.io.File;

import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.file.LineReader;
import eutil.strings.EStringUtil;

public class Cat extends FileCommand {
	
	public Cat() {
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "cat"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the content of a file."; }
	@Override public String getUsage() { return "ex: cat '.txt'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			if (args.size() == 0) termIn.error("Not enough arguments!");
			else if (args.size() >= 1) {
				String all = EStringUtil.combineAll(args, " ");
				File f = new File(all);
				
				if (all.startsWith("..")) f = new File(termIn.getDir(), args.get(0));
				
				if (f.exists()) displayFile(termIn, f);
				else {
					f = new File(termIn.getDir(), all);
					
					if (f.exists()) displayFile(termIn, f);
					else {
						if (args.get(0).startsWith("..")) f = new File(termIn.getDir(), args.get(0));
						else f = new File(args.get(0));
						
						if (f.exists()) displayFile(termIn, f);
						else {
							f = new File(termIn.getDir(), args.get(0));
							
							if (f.exists()) displayFile(termIn, f);
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
		if (fileIn.isDirectory()) {
			termIn.error(fileIn.getName() + " is a directory not a file!");
			return;
		}
		
		try (var reader = new LineReader(fileIn)) {
			if (reader.hasNextLine()) termIn.info("Displaying content:\n");
			reader.forEach(l -> termIn.writeln(l, EColors.lgray));
		}
		catch (Exception e) {
			error(termIn, e);
		}
	}
	
}

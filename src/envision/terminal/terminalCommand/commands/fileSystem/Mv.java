package envision.terminal.terminalCommand.commands.fileSystem;

import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.window.ETerminal;

public class Mv extends FileCommand {
	
	public Mv() {
		super(CommandType.NORMAL);
		numArgs = 2;
	}
	
	@Override public String getName() { return "mv"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Moves the contents of one file to another."; }
	@Override public String getUsage() { return "ex: mv 'src' 'dest'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() < 2) { termIn.error("Not enough arguments!"); }
		else if (args.size() == 2) {
			try {
				
				File f = new File(termIn.getDir(), args.get(0));
				
				//if the source file exists
				if (f.exists()) {
					File dest = new File(new File(System.getProperty("user.dir")), args.get(1));
					move(termIn, f, dest);
				}
				else { termIn.error("Error: Cannot find the object specified!"); }
				
			}
			catch (Exception e) {
				error(termIn, e);
			}
		}
		else if (args.size() > 2) { termIn.error("Too many arguments!"); }
	}
	
	private void move(ETerminal termIn, File src, File dest) {
		try {
			Files.move(Paths.get(src.getCanonicalPath()), Paths.get(dest.getCanonicalPath()), StandardCopyOption.REPLACE_EXISTING);
			termIn.writeln("Moved object to: " + EColors.white + dest, EColors.green);
		}
		catch (Exception e) {
			termIn.error("Error: Failed to move object! " + EColors.white + e.toString());
			error(termIn, e);
		}
	}
	
}
package engine.terminal.terminalCommand.commands.fileSystem;

import java.io.File;
import java.io.IOException;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class Pwd extends FileCommand {
	
	public Pwd() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "pwd"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("dir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current working directory."; }
	@Override public String getUsage() { return "ex: pwd"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.error("Too many arguments!");
			return;
		}
		
		try {
			String path = termIn.getDir().getCanonicalPath();
			String colorPath = "" + EColors.mc_aqua + path;
			termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
		}
		catch (IOException e) {
			error(termIn, e);
		}
	}
	
}


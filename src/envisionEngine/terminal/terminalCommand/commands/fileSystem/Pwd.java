package envisionEngine.terminal.terminalCommand.commands.fileSystem;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.window.ETerminal;
import java.io.File;
import java.io.IOException;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class Pwd extends FileCommand {
	
	public Pwd() {
		super(CommandType.NORMAL);
		numArgs = 0;
	}
	
	@Override public String getName() { return "pwd"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Prints the current working directory."; }
	@Override public String getUsage() { return "ex: pwd"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() == 0) {
			try {
				String path = termIn.getDir().getCanonicalPath();
				String colorPath = "" + EColors.mc_aqua + path;
				termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
			}
			catch (IOException e) {
				error(termIn, e);
			}
		}
		else { termIn.error("Too many arguments!"); }
	}
	
}


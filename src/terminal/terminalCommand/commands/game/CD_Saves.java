package terminal.terminalCommand.commands.game;

import java.io.File;
import main.Game;
import renderUtil.EColors;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;

public class CD_Saves extends TerminalCommand {
	
	public CD_Saves() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "worldsdir"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("wdir"); }
	@Override public String getHelpInfo(boolean runVisually) { return "CDs to the directory where worlds are stored."; }
	@Override public String getUsage() { return "ex: wdir"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command does not take arguments!"); termIn.info(getUsage()); }
		else {
			try {
				termIn.setDir(Game.settings.getEditorWorldsDir());
				String path = termIn.getDir().getCanonicalPath();
				String colorPath = "" + EColors.mc_aqua + path;
				termIn.writeLink("Current Dir: " + colorPath, path, new File(path), false, EColors.yellow);
			}
			catch (Exception e) {
				e.printStackTrace();
				error(termIn, e);
			}
		}
	}
	
}

package terminal.terminalCommand.commands.game;

import gameScreens.MainMenuScreen;
import main.Game;
import renderUtil.EColors;
import storageUtil.EArrayList;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;

public class UnloadWorld extends TerminalCommand {
	
	public UnloadWorld() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "unloadworld"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("uw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Unloads the current world (if any)"; }
	@Override public String getUsage() { return "ex: uw"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (Game.theWorld != null) {
			termIn.writeln("Unloading '" + Game.theWorld.getName() + "'", EColors.green);
			Game.loadWorld(null);
			Game.displayScreen(new MainMenuScreen());
		}
		else {
			termIn.writeln("No world to unload", EColors.yellow);
		}
	}
	
}

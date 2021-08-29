package debug.terminal.terminalCommand.commands.game;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.storage.EArrayList;
import gameScreens.MainMenuScreen;
import main.QoT;

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
		if (QoT.theWorld != null) {
			termIn.writeln("Unloading '" + QoT.theWorld.getName() + "'", EColors.green);
			QoT.loadWorld(null);
			QoT.displayScreen(new MainMenuScreen());
		}
		else {
			termIn.writeln("No world to unload", EColors.yellow);
		}
	}
	
}

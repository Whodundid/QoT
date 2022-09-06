package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;
import game.screens.main.MainMenuScreen;

public class UnloadWorld_CMD extends TerminalCommand {
	
	public UnloadWorld_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "unloadworld"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("uw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Unloads the current world (if any)"; }
	@Override public String getUsage() { return "ex: uw"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (QoT.theWorld != null) {
			termIn.writeln("Unloading world '" + QoT.theWorld.getWorldName() + "'", EColors.green);
			QoT.loadWorld(null);
			QoT.displayScreen(new MainMenuScreen());
		}
		else {
			termIn.writeln("No world to unload", EColors.yellow);
		}
	}
	
}

package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;
import world.GameWorld;

public class World_CMD extends TerminalCommand {
	
	public World_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "world"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays current world info"; }
	@Override public String getUsage() { return "ex: world"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			errorUsage(termIn, ERROR_NO_ARGS);
			return;
		}
		
		GameWorld world = QoT.theWorld;
		
		if (world == null) {
			termIn.error("No world loaded!");
		}
		else {
			String n = world.getName();
			int w = world.getWidth();
			int h = world.getHeight();
			boolean u = world.isUnderground();
			
			termIn.writeln(EColors.yellow + "Name: " + EColors.green + n);
			termIn.writeln(EColors.yellow + "Dims: " + EColors.green + "[" + w + "x" + h + "]");
			termIn.writeln(EColors.yellow + "Underground: " + EColors.green + u);
		}
	}
	
}

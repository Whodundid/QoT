package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;

public class FPS_CMD extends TerminalCommand {
	
	public FPS_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "fps"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("frames"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current game framerate."; }
	@Override public String getUsage() { return "ex: fps"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.error("This command does not take arguments!");
			termIn.info(getUsage());
		}
		else {
			termIn.writeln("FPS: " + QoT.getFPS(), EColors.lime);
		}
	}
	
}

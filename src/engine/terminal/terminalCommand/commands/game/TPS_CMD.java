package engine.terminal.terminalCommand.commands.game;

import engine.QoT;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

public class TPS_CMD extends TerminalCommand {
	
	public TPS_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "tps"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("ticks"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current game ticks per second."; }
	@Override public String getUsage() { return "ex: tps"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.error("This command does not take arguments!");
			termIn.info(getUsage());
		}
		else {
			termIn.writeln("TPS: " + QoT.getRunningTicks(), EColors.lime);
		}
	}
	
}

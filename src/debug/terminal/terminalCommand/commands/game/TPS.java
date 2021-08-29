package debug.terminal.terminalCommand.commands.game;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.storage.EArrayList;
import main.QoT;

public class TPS extends TerminalCommand {
	
	public TPS() {
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
			termIn.writeln("TPS: " + QoT.getTickrate(), EColors.lime);
		}
	}
	
}

package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.QoT;

public class TPS_CMD extends TerminalCommand {
	
	public TPS_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "tps"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ticks"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current game ticks per second."; }
	@Override public String getUsage() { return "ex: tps"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln("TPS: " + QoT.getTargetUPS(), EColors.lime);
		}
		else if (args.hasOne()) {
			String arg = args.get(0);
			try {
				int val = Integer.parseInt(arg);
				val = ENumUtil.clamp(val, 1, Integer.MAX_VALUE);
				QoT.setTargetUPS(val);
				termIn.writeln("Set game tickrate to " + val + " ticks per second!", EColors.lime);
			}
			catch (Exception e) {
				error(termIn, e);
				termIn.error("Expected a valid integer value!");
			}
		}
		else {
			errorUsage(termIn, ERROR_TOO_MANY);
		}
	}
	
}

package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import game.QoT;

public class TPS_CMD extends TerminalCommand {
	
	public TPS_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "tps"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("ticks"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current game ticks per second."; }
	@Override public String getUsage() { return "ex: tps"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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

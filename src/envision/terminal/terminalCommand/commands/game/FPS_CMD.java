package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import game.QoT;

public class FPS_CMD extends TerminalCommand {
	
	public FPS_CMD() {
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "fps"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("frames"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current game framerate."; }
	@Override public String getUsage() { return "ex: fps"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln("FPS: " + QoT.getFPS(), EColors.lime);
		}
		else if (args.hasOne()) {
			String arg = args.get(0);
			try {
				int val = Integer.parseInt(arg);
				val = ENumUtil.clamp(val, 1, Integer.MAX_VALUE);
				QoT.setTargetFPS(val);
				termIn.writeln("Set game framerate to " + val + " frames per second!", EColors.lime);
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

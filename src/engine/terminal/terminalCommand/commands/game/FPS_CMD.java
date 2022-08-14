package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;
import main.QoT;

public class FPS_CMD extends TerminalCommand {
	
	public FPS_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "fps"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("frames"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays or sets the current framerate."; }
	@Override public String getUsage() { return "ex: fps"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.size() > 1) {
			termIn.error(ERROR_TOO_MANY);
			return;
		}
		if (args.hasOne()) {
			String arg = args.get(0);
			try {
				int val = Integer.parseInt(arg);
				val = NumberUtil.clamp(val, 1, Integer.MAX_VALUE);
				QoT.setTargetFPS(val);
				termIn.writeln("Set game framerate to " + val + " frames per second!", EColors.lime);
			}
			catch (Exception e) {
				error(termIn, e);
				termIn.error("Expected a valid integer value!");
			}
		}
		else {
			termIn.writeln("FPS: " + QoT.getFPS(), EColors.lime);
		}
	}
	
}

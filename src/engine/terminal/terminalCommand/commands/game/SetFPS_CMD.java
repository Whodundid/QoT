package engine.terminal.terminalCommand.commands.game;

import engine.QoT;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

public class SetFPS_CMD extends TerminalCommand {
	
	public SetFPS_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "set_framerate"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("set_fps"); }
	@Override public String getHelpInfo(boolean runVisually) { return "modifies the game's framerate"; }
	@Override public String getUsage() { return "ex: set_fps 60"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			QoT.setTargetFPS(60);
			termIn.writeln("Set game framerate to 60 frames per second!", EColors.lime);
		}
		else if (args.hasOne()) {
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
	}
	
}

package envision.debug.terminal.commands.categories.engine;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.QoT;

public class CMD_FPS extends TerminalCommand {
	
	public CMD_FPS() {
		setCategory("Engine");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "fps"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("frames"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current engine framerate."; }
	@Override public String getUsage() { return "ex: fps"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln("FPS: " + QoT.getTargetFPS(), EColors.lime);
			return;
		}
		else if (args.size() > 1) {
			errorUsage(termIn, ERROR_TOO_MANY);
			return;
		}
		
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
	
}

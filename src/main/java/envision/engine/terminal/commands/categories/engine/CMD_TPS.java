package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CMD_TPS extends TerminalCommand {
	
	public CMD_TPS() {
		setCategory("Engine");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "tps"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ticks"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays the current engine ticks per second."; }
	@Override public String getUsage() { return "ex: tps"; }
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(1);
	    
		if (noArgs()) {
	         writeln(EColors.yellow, "target: ", Envision.getTargetTPS());
	         writeln(EColors.green, "actual: ", Envision.getTPS());
	         return;
		}
		
        try {
            int val = Integer.parseInt(firstArg());
            val = ENumUtil.clamp(val, 1, Integer.MAX_VALUE);
            Envision.setTargetUPS(val);
            writeln("Set game tickrate to " + val + " ticks per second!", EColors.lime);
        }
        catch (Exception e) {
            error(e);
            error("Expected a valid integer value!");
        }
	}
	
}

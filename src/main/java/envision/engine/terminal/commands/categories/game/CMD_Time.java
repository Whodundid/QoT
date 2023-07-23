package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.math.ENumUtil;

public class CMD_Time extends TerminalCommand {
	
	public CMD_Time() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "time"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Gets or Sets the time of the world"; }
	@Override public String getUsage() { return "ex: time set 0"; }
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(2);
	    
		if (Envision.theWorld == null) {
			error("Current World is Null");
			return;
		}
		
		int curTime = Envision.theWorld.getTime();
		
		writeln(EColors.yellow, "Current Time: ", EColors.lgreen, curTime);
		
		if (argLength() > 1) {
		    Envision.theWorld.setTime(ENumUtil.parseInt(arg(1), 0));
		}
	}
	
}

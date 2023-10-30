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
		
		if (argLength() > 1) {
		    String arg = arg(1);
		    arg = arg.toLowerCase();
		    switch (arg) {
		    // the start of the sunrise
		    // 6:00 am
		    case "sunrise":
		    case "dawn":
		        
		    // mid morning
		    // 9:00 am
		    case "morning":
		        
		    // mid day
		    // 12:00 pm
		    case "noon":
		    
		    // beginning of sunset
		    // 7:00 pm
		    case "sunset":
		    case "dusk":
		    
		    // 10:00 pm
		    case "night":
		        
		    // 12:00 am
		    case "midnight":
		    }
		    Envision.theWorld.setTime(ENumUtil.parseInt(arg(1), 0));
		}
		
		int curTime = Envision.theWorld.getTime() - 1;
		int maxTime = Envision.theWorld.getDayLength();
		
		writeln(EColors.yellow, "Current Time: ", EColors.lgreen, curTime, "/", maxTime);
	}
	
}

package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
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
    public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
        basicTabComplete(termIn, args, EList.of("dawn", "morning", "noon", "sunset", "night", "midnight"));
    }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(2);
        
        if (Envision.levelManager == null) {
            error("Current level is Null");
            return;
        }
        
        int maxTime = Envision.levelManager.getDayLength();
        
        if (argLength() > 1) {
            String arg = arg(1);
            arg = arg.toLowerCase();
            switch (arg) {
            // the start of the sunrise
            // 6:00 am
            case "sunrise":
            case "dawn":
                Envision.levelManager.setTime((int) (maxTime * 0.15));
                break;
            // mid morning
            // 9:00 am
            case "morning":
                Envision.levelManager.setTime((int) (maxTime * 0.30));
                break;
            // mid day
            // 12:00 pm
            case "noon":
                Envision.levelManager.setTime((int) (maxTime * 0.50));
                break;
            // beginning of sunset
            // 7:00 pm
            case "sunset":
            case "dusk":
                Envision.levelManager.setTime((int) (maxTime * 0.70));
                break;
            // 10:00 pm
            case "night":
                Envision.levelManager.setTime((int) (maxTime * 0.85));
                break;
            // 12:00 am
            case "midnight":
                Envision.levelManager.setTime((int) (maxTime * 0.0));
                break;
            default:
                Envision.levelManager.setTime(ENumUtil.parseInt(arg(1), 0));
            }
        }
        
        int curTime = Envision.levelManager.getTime();
        
        writeln(EColors.yellow, "Current Time: ", EColors.lgreen, curTime, "/", maxTime);
    }
    
}

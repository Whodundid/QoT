package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CMD_FPS extends TerminalCommand {
    
    public CMD_FPS() {
        setCategory("Engine");
        expectedArgLength = 0;
    }
    
    @Override public String getName() { return "fps"; }
    @Override public EList<String> getAliases() { return EList.of("frames"); }
    @Override public String getHelpInfo(boolean runVisually) { return "Displays the current engine framerate."; }
    @Override public String getUsage() { return "ex: fps"; }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(1);
        
        if (noArgs()) {
            writeln(EColors.yellow, "target: ", Envision.getTargetFPS());
            writeln(EColors.green, "actual: ", Envision.getFPS());
            return;
        }
        
        try {
            int val = Integer.parseInt(firstArg());
            val = ENumUtil.clamp(val, 1, Integer.MAX_VALUE);
            Envision.setTargetFPS(val);
            writeln(EColors.lime, "Set game framerate to ", val, " frames per second!");
        }
        catch (Exception e) {
            error(e);
            error("Expected a valid integer value!");
        }
    }
    
}

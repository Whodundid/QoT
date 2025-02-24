package envision.engine.internal.kernel.terminal.commands.categories.system;

import envision.Envision;
import envision.debug.Profiler;
import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_Profiler extends TerminalCommand {
    
    public CMD_Profiler() {
        setCategory("System");
        expectedArgLength = 0;
    }

    @Override public String getName() { return "profiler"; }
    @Override public EList<String> getAliases() { return EList.of("prof"); }
    @Override public String getHelpInfo(boolean runVisually) { return "Displays a profiler's result by profiler name"; }
    @Override public String getUsage() { return "ex: profiler game_loop"; }
    
    @Override
    public void runCommand() {
        expectNoMoreThan(1, "This command either takes zero arguments or the name of a profiler to display!");
        
        if (noArgs()) {
            var gameLoopProfiler = Envision.LAST_GAME_LOOP_PROFILER;
            if (gameLoopProfiler == null) return;
            String out = gameLoopProfiler.createProfilerResultString();
            writeln(out, EColors.lgreen);
            return;
        }
        
        String name = firstArg();
        switch (name) {
        case "GAME_TICK":
            displayProfiler(name, Envision.LAST_GAME_TICK_PROFILER);
            break;
        case "FRAME_TICK":
            displayProfiler(name, Envision.LAST_FRAME_TICK_PROFILER);
            break;
        default:
            var profiler = Profiler.getProfiler(name);
            displayProfiler(name, profiler);
        }
    }
    
    private void displayProfiler(String name, Profiler profiler) {
        if (profiler == null) {
            error("There is no profiler under the given name: '" + name + "'");
        }
        else {
            String out = profiler.createProfilerResultString();
            writeln(out, EColors.lgreen);
        }
    }
    
}

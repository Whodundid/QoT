package envision.engine.internal.kernel.terminal.commands.categories.system;

import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CMD_Watch extends TerminalCommand {
    
    public CMD_Watch() {
        setCategory("System");
        expectedArgLength = -1;
    }

    @Override public String getName() { return "watch"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Continuously watches the results of a command"; }
    @Override public String getUsage() { return "ex: watch fps | watch 1000 fps"; }
    
    @Override
    public void runCommand() {
        expectAtLeast(1, "Expected at least 1 argument: a command name to watch!");
        
        String command = firstArg();
        EList<String> commandArgs = subArgs(1, argLength());
        
        float interval = 3000.0f;
        
        if (ENumUtil.isNumber(command)) {
            expectAtLeast(2, "If providing an interval as the 1st argument, then the command must be the 2nd argument.");
            interval = Float.parseFloat(command);
            // shift command and arguments over
            command = secondArg();
            commandArgs = subArgs(2, argLength());
        }
        
        if (getName().equals(command)) {
            error("Watch command cannot watch itself!");
            return;
        }
        
        term.setWatchCommand(command, commandArgs, interval);
    }
    
}

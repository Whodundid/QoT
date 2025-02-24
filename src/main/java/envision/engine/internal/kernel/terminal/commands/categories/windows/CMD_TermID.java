package envision.engine.internal.kernel.terminal.commands.categories.windows;

import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import eutil.colors.EColors;

public class CMD_TermID extends TerminalCommand {
    
    public CMD_TermID() {
        setCategory("Windows");
        expectedArgLength = 0;
    }

    @Override public String getName() { return "id"; }
    @Override public String getHelpInfo(boolean runVisually) { return "returns the window id for this terminal"; }
    @Override public String getUsage() { return "ex: id"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void runCommand() {
        writeln(EColors.yellow, term().getObjectName(), EColors.lgray, " Window ID: ", term().getObjectID());
    }
    
}

package envision.engine.kernel.terminal.commands.categories.system;

import envision.engine.kernel.terminal.commands.TerminalCommand;
import envision.engine.kernel.user.UserProfileRegistry;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class CMD_Exit extends TerminalCommand {
    
    public CMD_Exit() {
        setCategory("System");
    }

    @Override public String getName() { return "exit"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Exits the current process"; }
    @Override public String getUsage() { return "ex: exit"; }
    @Override public byte requiredPermissionLevel() { return 0; }
    
    @Override
    public void runCommand() {
        expectNoArgs();
        
        if (term().getUserStackSize() > 1) {
            writeln(EColors.lgray, "exiting...");
            term().popUser();
            EColors uc = UserProfileRegistry.getProfileEColor(user());
            writeln(uc, user().getName(), EColors.white, " >");
        }
        else {
            term().close();
        }
    }
    
}

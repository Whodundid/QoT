package envision.engine.kernel.terminal.commands.categories.system;

import envision.engine.kernel.terminal.commands.TerminalCommand;
import envision.engine.kernel.user.UserProfileRegistry;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class CMD_WhoAmI extends TerminalCommand {
    
    public CMD_WhoAmI() {
        setCategory("System");
        expectedArgLength = 0;
    }

    @Override public String getName() { return "whoami"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Provides info on the current user."; }
    @Override public String getUsage() { return "ex: whoami"; }
    @Override public byte requiredPermissionLevel() { return 0; }
    
    @Override
    public void runCommand() {
        expectNoArgs();
        var user = user();
        EColors uc = UserProfileRegistry.getProfileEColor(user());
        writeln(uc, user.getName(), EColors.lgray, ": permissions=", user.getPermissionLevel());
    }
    
}

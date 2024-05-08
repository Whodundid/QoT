package envision.engine.kernel.terminal.commands.categories.system;

import envision.engine.kernel.EnvisionKernel;
import envision.engine.kernel.terminal.commands.TerminalCommand;
import envision.engine.kernel.terminal.window.ETerminalWindow;
import envision.engine.kernel.user.UserProfileRegistry;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

// Author: Hunter Bragg

public class CMD_SU extends TerminalCommand {
    
    public CMD_SU() {
        setCategory("System");
        expectedArgLength = 1;
    }
    
    @Override public String getName() { return "su"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Used to switch between user profiles"; }
    @Override public String getUsage() { return "ex: su dev"; }
    @Override public byte requiredPermissionLevel() { return 0; }
    
    @Override
    public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
        var names = EnvisionKernel.getInstance().getProfileRegistry().getRegisteredProfileNames();
        basicTabComplete(termIn, args, names);
    }
    
    @Override
    public void runCommand() {
        expectExactly(1, "Error! No username specified!");
        
        final var username = firstArg();
        final var registry = EnvisionKernel.getInstance().getProfileRegistry();
        final var profile = registry.getProfile(username);
        
        if (profile != null) {
            term().switchUser(profile);
            EColors uc = UserProfileRegistry.getProfileEColor(user());
            writeln(uc, term().getActiveUser().getName(), EColors.white, " >");
        }
        else {
            term.writeln(EColors.lred, "Cannot switch to a NULL user!");
        }
    }
    
}

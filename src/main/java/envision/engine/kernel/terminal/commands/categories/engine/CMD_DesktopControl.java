package envision.engine.kernel.terminal.commands.categories.engine;

import envision.engine.kernel.developerDesktop.DeveloperDesktop;
import envision.engine.kernel.terminal.commands.TerminalCommand;

//Author: Hunter Bragg

public class CMD_DesktopControl extends TerminalCommand {
    
    public CMD_DesktopControl() {
        setCategory("System");
        expectedArgLength = 1;
    }
    
    @Override public String getName() { return "desktop"; }
    @Override public String getHelpInfo(boolean runVisually) { return "provies an interface to the DeveloperDesktop"; }
    @Override public String getUsage() { return "ex: desktop reload"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void runCommand() {
        expectAtLeast(1);
        
        if (oneArg()) {
            switch (firstArg()) {
            case "rel":
            case "reload":
                reloadDesktop();
                break;
            case "s":
            case "save":
                saveDesktopConfig();
                break;
            default:
                errorUsage("Invalid action argument!");
            }
        }
    }
    
    private void reloadDesktop() {
        DeveloperDesktop.reloadConfig();
    }
    
    private void saveDesktopConfig() {
        DeveloperDesktop.saveConfig();
    }
    
}

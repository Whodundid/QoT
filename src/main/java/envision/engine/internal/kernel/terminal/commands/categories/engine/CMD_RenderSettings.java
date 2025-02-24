package envision.engine.internal.kernel.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.internal.kernel.developerDesktop.windows.GameOptionsWindow;
import envision.engine.internal.kernel.terminal.commands.TerminalCommand;
import envision.engine.screens.ScreenLevel;
import eutil.datatypes.util.EList;

public class CMD_RenderSettings extends TerminalCommand {
    
    public CMD_RenderSettings() {
        setCategory("Engine");
        expectedArgLength = 0;
    }
    
    @Override public String getName() { return "renderer"; }
    @Override public EList<String> getAliases() { return EList.of("rset"); }
    @Override public String getHelpInfo(boolean runVisually) { return "Provides a means for interfacing with the game's rendering engine"; }
    @Override public String getUsage() { return "ex: rset'"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void runCommand() {
        Envision.displayWindow(ScreenLevel.TOP, new GameOptionsWindow());
    }
    
}

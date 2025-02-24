package envision.engine.internal.kernel.terminal.commands.categories.fileSystem;

import java.io.File;

import envision.engine.internal.kernel.developerDesktop.windows.fileExplorer.FileExplorerWindow;
import envision.engine.internal.kernel.terminal.terminalUtil.TerminalCommandError;
import eutil.datatypes.util.EList;

public class CMD_Files extends AbstractFileCommand {
    
    public CMD_Files() {
        expectedArgLength = -1;
    }

    @Override public String getName() { return "files"; }
    @Override public EList<String> getAliases() { return EList.of("explorer"); }
    @Override public String getHelpInfo(boolean runVisually) { return "Opens a new file explorer window"; }
    @Override public String getUsage() { return "ex: files"; }
    
    @Override
    public void runCommand() {
        if (noArgs()) {
            displayWindow(new FileExplorerWindow(dir()), false);
            return;
        }
        
        var args = args();
        for (String a : args) {
            try {
                File dir = new File(a);
                expectDirectory(dir);
                displayWindow(new FileExplorerWindow(dir), false);
            }
            catch (TerminalCommandError e) {
                e.printStackTrace();
            }
        }
    }
    
}

package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.datatypes.util.EList;

public class DesktopShortcut_Command extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private TerminalCommand theCommand;
    private EList<String> args;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Command(int x, int y, TerminalCommand commandIn, EList<String> argsIn) {
        super(x, y, ShortcutType.COMMAND);
        theCommand = commandIn;
        args = argsIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        theCommand.runCommand(null, args, false);
    }
    
}

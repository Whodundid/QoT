package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.scripting.EnvisionScript;
import eutil.datatypes.util.EList;

public class DesktopShortcut_Script extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private EnvisionScript theScript;
    private EList<String> args;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Script(int x, int y, EnvisionScript scriptIn, EList<String> argsIn) {
        super(x, y, ShortcutType.SCRIPT);
        theScript = scriptIn;
        args = argsIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        
    }
    
}

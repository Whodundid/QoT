package envision.engine.windows.developerDesktop.shortcuts;

import java.io.File;

public class DesktopShortcut_File extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private File theFile;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_File(int x, int y, File fileIn) {
        super(x, y, ShortcutType.FILE);
        theFile = fileIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        
    }
    
}

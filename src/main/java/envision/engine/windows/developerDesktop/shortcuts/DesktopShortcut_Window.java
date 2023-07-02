package envision.engine.windows.developerDesktop.shortcuts;

import envision.Envision;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;

public class DesktopShortcut_Window extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private IWindowParent theWindow;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Window(int x, int y, IWindowParent windowIn) {
        super(x, y, ShortcutType.WINDOW);
        theWindow = windowIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        if (theWindow == null) return;
        
        try {
            var theClass = theWindow.getClass();
            var constructor = theClass.getConstructor();
            if (constructor != null) {
                var newInstance = (IWindowParent) constructor.newInstance();
                Envision.getTopScreen().displayWindow(newInstance, ObjectPosition.EXISTING_OBJECT_INDENT);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

package envision.engine.windows.developerDesktop.shortcuts;

import envision.Envision;
import envision.engine.assets.WindowTextures;
import envision.engine.windows.WindowRegistry;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class DesktopShortcut_Window extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private String windowCategoryName;
    private String windowName;
    private Class<? extends IWindowParent> theWindow;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Window(String shortcutName) {
        super(shortcutName, ShortcutType.WINDOW);
    }
    
    public DesktopShortcut_Window(int x, int y, IWindowParent windowIn) {
        this(windowIn.getObjectName(), x, y, windowIn);
    }
    
    public DesktopShortcut_Window(String shortcutName, int x, int y, IWindowParent windowIn) {
        super(shortcutName, x, y, ShortcutType.WINDOW);
        theWindow = windowIn.getClass();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        if (theWindow == null) return;
        
        try {
            var constructor = theWindow.getConstructor();
            if (constructor != null) {
                // This should probably be made a bit more robust in the future...
                var newInstance = (IWindowParent) constructor.newInstance();
                Envision.getDeveloperDesktop().displayWindow(newInstance, ObjectPosition.EXISTING_OBJECT_INDENT);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void openShortcutRCM() {
        createBaseShortcutRCM();
        
        shortcutRCM.addOption("Open", WindowTextures.file_up, this::onDoubleClick);
        shortcutRCM.addOption("Copy", this::copyShortcut);
        shortcutRCM.addOption("Delete", WindowTextures.red_x, this::deleteShortcut);
        shortcutRCM.addOption("Rename", this::renameShortcut);
        shortcutRCM.addOption("Properties", this::openProperties);
        
        shortcutRCM.showOnCurrent();
    }
    
    @Override
    public void generateSaveString(EStringBuilder sb) {
        generateBaseSaveString(sb);
        sb.println("target: ", windowCategoryName, ";", windowName);
        sb.decrementTabCount();
    }
    
    //=========
    // Methods
    //=========
    
    public void parseWindowTarget(String targetIn) {
        if (EStringUtil.isNotPopulated(targetIn)) return;
        
        var stringParts = targetIn.split(";");
        
        // only care if there is actually data to process
        if (stringParts.length < 2) return;
        
        if (stringParts.length == 2) {
            String categoryName = stringParts[0];
            String targetName = stringParts[1];
            
            var category = WindowRegistry.getRegisteredWindowsForCategory(categoryName);
            if (category == null) {
                Envision.error("No window category found for name: '" + categoryName + "'");
                return;
            }
            
            var windowClass = category.get(targetName);
            if (windowClass == null) {
                Envision.error("No window associated with '" + targetName + "' under category of '" + categoryName + "'");
                return;
            }
            
            windowCategoryName = categoryName;
            windowName = targetName;
            setWindowTarget(windowClass);
        }
    }
    
    public void setWindowTarget(IWindowParent windowIn) {
        if (windowIn == null) return;
        setWindowTarget(windowIn.getClass());
    }
    
    public void setWindowTarget(Class<? extends IWindowParent> windowClassIn) {
        theWindow = windowClassIn;
        
        try {
            var constructor = theWindow.getConstructor();
            if (constructor != null) {
                // This should probably be made a bit more robust in the future...
                var newInstance = (IWindowParent) constructor.newInstance();
                this.setIcon(newInstance.getWindowIcon());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

package envision.engine.windows;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import envision.debug.testStuff.TestTextureSheetBuilder;
import envision.engine.windows.bundledWindows.GLKeyChecker;
import envision.engine.windows.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.datatypes.util.EList;

public class WindowRegistry {
    
    private static final Map<String, Class<? extends IWindowParent>> registeredWindows = new HashMap<>();
    private static boolean loaded = false;
    
    public static void loadBaseWindows() {
        if (loaded) return;
        
        registerWindow("keys", GLKeyChecker.class);
        registerWindow("colors", ColorPickerSimple.class);
        registerWindow("test-texture-sheet-builder", TestTextureSheetBuilder.class);
        
        loaded = true;
    }
    
    public static void registerWindow(String windowName, IWindowParent window) {
        registerWindow(windowName, window.getClass());
    }
    
    public static void registerWindow(String windowName, Class<? extends IWindowParent> windowClass) {
        if (windowName == null || windowClass == null) return;
        registeredWindows.put(windowName, windowClass);
    }
    
    public static Map<String, Class<? extends IWindowParent>> getRegisteredWindows() {
        return Collections.unmodifiableMap(registeredWindows);
    }
    
    public static EList<String> getWindowNames() { return EList.of(registeredWindows.keySet()); }
    
}

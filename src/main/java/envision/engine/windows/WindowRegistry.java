package envision.engine.windows;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import envision.debug.testStuff.SpriteSheetBuilder;
import envision.debug.testStuff.TestTextureSheetBuilder;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.CalculatorWindow;
import envision.engine.windows.bundledWindows.GLKeyChecker;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.datatypes.util.EList;

public class WindowRegistry {
    
    private WindowRegistry() {}
    
    public static final String ENVISION_WINDOW_CATEGORY_NAME = "ENVISION";
    private static final Map<String, Map<String, Class<? extends IWindowParent>>> REGISTERED_WINDOWS = new HashMap<>();
    public static final boolean LOADED;
    
    static {
        registerWindow("terminal", ETerminalWindow.class);
        registerWindow("file-explorer", FileExplorerWindow.class);
        registerWindow("text-editor", TextEditorWindow.class);
        registerWindow("calculator", CalculatorWindow.class);
        registerWindow("keys", GLKeyChecker.class);
        registerWindow("colors", ColorPickerSimple.class);
        registerWindow("test-texture-sheet-builder", TestTextureSheetBuilder.class);
        registerWindow("sprite-sheet-builder", SpriteSheetBuilder.class);
        
        LOADED = true;
    }
    
    public static void registerWindow(String windowName, IWindowParent window) {
        registerWindow(ENVISION_WINDOW_CATEGORY_NAME, windowName, window);
    }
    
    public static void registerWindow(String categoryName, String windowName, IWindowParent window) {
        registerWindow(categoryName, windowName, window.getClass());
    }
    
    public static void registerWindow(String windowName, Class<? extends IWindowParent> windowClass) {
        registerWindow(ENVISION_WINDOW_CATEGORY_NAME, windowName, windowClass);
    }
    
    public static void registerWindow(String categoryName, String windowName, Class<? extends IWindowParent> windowClass) {
        if (windowName == null || windowClass == null) return;
        var category = REGISTERED_WINDOWS.computeIfAbsent(categoryName, s -> new HashMap<>());
        category.put(windowName, windowClass);
    }
    
    public static Map<String, Class<? extends IWindowParent>> getRegisteredWindowsForCategory(String categoryName) {
        var category = REGISTERED_WINDOWS.getOrDefault(categoryName, new HashMap<>());
        return Collections.unmodifiableMap(category);
    }
    
    public static EList<String> getCategoryNames() {
        return EList.of(REGISTERED_WINDOWS.keySet());
    }
    
    public static EList<String> getWindowNamesForCategory(String categoryName) {
        var category = REGISTERED_WINDOWS.getOrDefault(categoryName, new HashMap<>());
        return EList.of(category.keySet());
    }
    
    public static EList<Map<String, Class<? extends IWindowParent>>> getAllRegisteredWindows() {
        return REGISTERED_WINDOWS.values()
                                 .stream()
                                 .collect(EList.toEList());
    }
    
    public static EList<String> getAllWindowNames() {
        return REGISTERED_WINDOWS.values()
                                 .stream()
                                 .flatMap(a -> a.keySet().stream())
                                 .collect(EList.toEList());
    }
    
}

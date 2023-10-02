package envision.engine.windows.developerDesktop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.developerDesktop.config.DesktopConfig;
import envision.engine.windows.developerDesktop.config.DesktopConfigParser;
import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut;
import envision.engine.windows.developerDesktop.taskbar.TaskBar;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import envision.engine.windows.windowTypes.TopWindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import qot.settings.QoTSettings;

public class DeveloperDesktop extends TopWindowParent {
    
    /** The directory for where content for this desktop is located. */
    public static final File DESKTOP_DIR = new File(QoTSettings.getLocalGameDir(), "desktop");
    /** The main config file location for the developer desktop. */
    public static final File DESKTOP_CONFIG_FILE = new File(DeveloperDesktop.DESKTOP_DIR, "desktop_config.ini");
    /** The main config file for the developer desktop. */
    private static final DesktopConfig DESKTOP_CONFIG = new DesktopConfig();
    
    // setup the desktop directory upfront
    static {
        if (!DESKTOP_DIR.exists()) DESKTOP_DIR.mkdirs();
        
        // load desktop config on startup
        DesktopConfigParser.loadConfigFile();
    }
    
    public static void loadConfig() {
        synchronized (DESKTOP_CONFIG) {
            DesktopConfigParser.loadConfigFile();
        }
    }
    
    public static void saveConfig() {
        synchronized (DESKTOP_CONFIG) {
            DesktopConfigParser.saveConfigFile();
        }
    }
    
    public static void reloadConfig() {
        synchronized (DESKTOP_CONFIG) {
            DesktopConfigParser.loadConfigFile();
            buildDesktopFromConfig();
        }
    }
    
    public static DesktopConfig getDesktopConfig() {
        synchronized (DESKTOP_CONFIG) {
            return DESKTOP_CONFIG;            
        }
    }
    
    public static boolean isConfigParsed() {
        synchronized (DESKTOP_CONFIG) {
            return DESKTOP_CONFIG.isParsed();            
        }
    }
    
    public static void buildDesktopFromConfig() {
        // don't try to build until the instance has actually been initialized
        if (instance == null) return;
        
        synchronized (DESKTOP_CONFIG) {
            if (!DESKTOP_CONFIG.isParsed()) return;
            
            var desktop = instance;
            var shortcuts = desktop.shortcuts;
            
            for (var s : shortcuts) desktop.removeObject(s);
            desktop.shortcuts.clear();
            
            var parsedShortcuts = DESKTOP_CONFIG.getShortcuts();
            for (var s : parsedShortcuts) {
                s.setParent(desktop);
                desktop.addObject(s);
                desktop.shortcuts.add(s);
                s.setHidden(!isOpen());
            }
        }
    }
    
    //==========================
    // Developer Desktop Screen
    //==========================
    
    public static DeveloperDesktop instance;
    private static boolean hasFocus = false;
    private static TaskBar taskBar;
    private final EList<DesktopShortcut> shortcuts = EList.newList();
    private final EList<IWindowParent> highlightedWindows = EList.newList();
    
    public static DeveloperDesktop getInstance() {
        if (instance == null) {
            instance = new DeveloperDesktop();
        }
        return instance;
    }
    
    private DeveloperDesktop() {
        res = Envision.getWindowDims();
        initChildren();
    }
    
    public void onRenderTick() {
        updateBeforeNextDraw(Mouse.getMx(), Mouse.getMy());
        //if (getChildren().isEmpty()) hasFocus = false;
        
        if (hasFocus) {
            drawRect(EColors.dsteel.opacity(140));
            
            int borderColor = 0xaaff0000;
            int borderWidth = 4;
            var b = getTaskBar();
            if (b != null && !b.isHidden()) {
                double ds = TaskBar.drawSize();
                drawRect(0, ds, borderWidth, height, borderColor); //left
                drawRect(borderWidth, ds, width - borderWidth, ds + borderWidth, borderColor); //top
                drawRect(width - borderWidth, ds, width, height, borderColor); //right
                drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
            }
            else {
                drawRect(0, 0, borderWidth, height, borderColor); //left
                drawRect(borderWidth, 0, width - borderWidth, borderWidth, borderColor); //top
                drawRect(width - borderWidth, 0, width, height, borderColor); //right
                drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
            }
            
            //drawString("Debug", borderWidth + 2, endY - borderWidth - FontRenderer.FONT_HEIGHT, EColors.dgray);
        }
        
        if (isVisible()) {
            int mX = Mouse.getMx();
            int mY = Mouse.getMy();
            
            //reset highlighted
            highlightedWindows.clear();
            
            if (!hasFirstDraw()) onFirstDraw_i();
            
            //draw this object first
            drawObject(mX, mY);
            
            // draw all shortcuts on desktop first
            for (var s : shortcuts) {
                drawWindowObject(s);
            }
            
            //now draw all child objects on top of parent
            for (var o : getChildren()) {
                //don't draw this here
                if (o instanceof DesktopShortcut) continue;
                
                drawWindowObject(o);
            }
            
            //draw highlighted window borders
            for (IWindowParent p : highlightedWindows) {
                if (p == null) continue;
                p.drawHighlightBorder();
            }
            
            //notify hover object
            var hoveringObject = getHoveringObject();
            if (hoveringObject != null) hoveringObject.onMouseHover(Mouse.getMx(), Mouse.getMy());
        }
        
        //draw debug stuff
        if (Envision.isDebugMode()) drawDebugInfo();
        
        //draw taskbar on top everything
        //if (taskBar != null) taskBar.drawObject_i(mX, mY);
        
        //draw game fps
        if (QoTSettings.drawFPS.getBoolean()) {
            String s = "FPS: " + Envision.getFPS();
            int s_width = FontRenderer.strWidth(s);
            drawString(s, Envision.getWidth() - 10.0 - s_width, 10.0);
        }
    }
    
    @Override
    public void handleMouseInput(int action, int mXIn, int mYIn, int button, int change) {
        if (hasFocus) {
            super.handleMouseInput(action, mXIn, mYIn, button, change);
            //check if desktop rcm should open
            DesktopRCM.checkOpen(action, button);
        }
        else if (Envision.currentScreen != null) {
            Envision.currentScreen.handleMouseInput(action, mXIn, mYIn, button, change);
        }
    }
    
    @Override
    public void handleKeyboardInput(int action, char typedChar, int keyCode) {
        //debug terminal
        if (action == 1 && Keyboard.isAltDown() && keyCode == Keyboard.KEY_TILDE) {
            if (Envision.currentScreen != null) {
                if (!isTerminalOpen()) {
                    displayWindow(new ETerminalWindow());
                    setFocused(true);
                }
                else if (!hasFocus()) {
                    setFocused(true);
                    ETerminalWindow term = getTerminalInstance();
                    if (term != null) term.requestFocus();
                }
            }
            else {
                var term = getTerminalInstance();
                if (term != null) term.requestFocus();
                else displayWindow(new ETerminalWindow());
                setFocused(true);
            }
        }
        //display framerate
        else if (action == 1 && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            QoTSettings.drawFPS.set(!QoTSettings.drawFPS.get());
        }
        else if (hasFocus) {
            super.handleKeyboardInput(action, typedChar, keyCode);
        }
        else if (Envision.currentScreen != null) {
            Envision.currentScreen.handleKeyboardInput(action, typedChar, keyCode);
        }
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESC) && getEscapeStopper() == null) {
            hasFocus = false;
            hideUnpinnedObjects();
        }
        else if (keyCode == Keyboard.KEY_F5) reloadConfig();
        else super.keyPressed(typedChar, keyCode);
    }
    
    public void clear() {
        removeAllObjects();
    }
    
    public void addTaskBar(TaskBar b) {
        if (taskBar == null) removeObject(taskBar);
        addObject(taskBar = b);
    }
    
    public void addTaskBar(boolean fromScratch) {
        addObject(taskBar = new TaskBar(fromScratch));
    }
    
    public TaskBar getTaskBar() {
        var objects = EList.of(getChildren());
        objects.removeAll(getRemovingChildren());
        objects.addAll(getAddingChildren());
        
        if (taskBar != null) return taskBar;
        else {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof TaskBar b) {
                    return taskBar = b;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public boolean hasFocus() {
        return hasFocus;
    }
    
    public void setFocused(boolean val) {
        hasFocus = val;
        if (hasFocus) {
            revealHiddenObjects();
            if (getTaskBar() == null) addTaskBar(true);
        }
        else hideUnpinnedObjects();
    }
    
    @Override
    public void onScreenResized() {
        super.onScreenResized();
        if (taskBar != null) taskBar.onScreenResized();
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public void onSystemDragAndDrop(EList<String> droppedFileNames) {
        if (!DeveloperDesktop.isOpen()) return;
        
        var obj = Envision.getDeveloperDesktop().getHighestZObjectUnderMouse();
        
        if (obj != null && obj.allowsSystemDragAndDrop()) {
            obj.onSystemDragAndDrop(droppedFileNames);
        }
        else {
            for (String fileName : droppedFileNames) {
                DeveloperDesktop.openFileAtMouse(fileName);
            }
        }
    }
    
    public static boolean isOpen() {
        return hasFocus;
    }
    
    /** Returns the list of all shortcuts actively on the desktop. */
    public EList<DesktopShortcut> getShortcuts() {
        return shortcuts;
    }
    
    //=====================================
    // Developer Desktop Directory Helpers
    //=====================================
    
    public static void addShortcut(DesktopShortcut shortcut) {
        if (shortcut == null) return;
        
        shortcut.setParent(instance);
        instance.shortcuts.add(shortcut);
        instance.addObject(shortcut);
        DESKTOP_CONFIG.addShortcut(shortcut);
        DeveloperDesktop.saveConfig();
        DeveloperDesktop.reloadConfig();
    }
    
    public static void removeShortcut(DesktopShortcut shortcut) {
        if (shortcut == null) return;
        
        instance.shortcuts.remove(shortcut);
        instance.removeObject(shortcut);
        DESKTOP_CONFIG.removeShortcut(shortcut);
        DeveloperDesktop.saveConfig();
        DeveloperDesktop.reloadConfig();
    }
    
    public static File createDirectoryOnDesktop(String dirName) {
        try {
            return createDirectoryInDir(DESKTOP_DIR, dirName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static File createFileOnDesktop(String fileName) {
        try {
            return createFileInDir(DESKTOP_DIR, fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //======================
    // File copy operations
    //======================
    
    private static final EList<File> filesToCopy = EList.newList();
    private static boolean isCutOperation;
    
    public static void setFilesToCopy(File... filesToCopyIn) { setFilesToCopy(EList.of(filesToCopyIn)); }
    public static void setFilesToCopy(Collection<File> filesToCopyIn) {
        filesToCopy.clearThenAddAll(filesToCopyIn);
        isCutOperation = false;
    }
    
    public static void setFilesToCut(File... filesToCutIn) { setFilesToCut(EList.of(filesToCutIn)); }
    public static void setFilesToCut(Collection<File> filesToCutIn) {
        filesToCopy.clearThenAddAll(filesToCutIn);
        isCutOperation = true;
    }
    
    public static void completeCutOperation() {
        filesToCopy.clear();
        isCutOperation = false;
    }
    
    public static EList<File> getWorkingFiles() { return filesToCopy; }
    public static boolean isCutOperation() { return isCutOperation; }
    /** Returns true if the given file matches the current working file to be cut. */
    public static boolean checkFileForCut(File fileIn) {
        return fileIn != null && filesToCopy.contains(fileIn) && isCutOperation;
    }
    /** Returns true if the given file matches the current working file to be cut. */
    public static boolean checkFileForCopy(File fileIn) {
        return fileIn != null && filesToCopy.contains(fileIn) && !isCutOperation;
    }
    /** Returns true if there is a file to copy. */
    public static boolean hasFilesToCopy() { return filesToCopy.isNotEmpty(); }
    
    /**
     * Performs the file operation that is currently in progress (if there is one).
     * 
     * @param directory The directory to perform the operation in
     * @return true if successful
     */
    public static boolean performFileOperation(File directory) {
        if (filesToCopy.isEmpty()) return false;
        
        return (isCutOperation) ? performCut(directory) : performCopy(directory);
    }
    
    /**
     * Performs the file copy operation by pasting the current working file
     * into the given directory.
     * 
     * @param dir The directory to copy into
     * @return True if successfully copied
     */
    public static boolean performCopy(File dir) {
        // make sure there is even a file to copy
        if (filesToCopy.isEmpty()) return false;
        // make sure the directory we are copying into actually exists
        if (!EFileUtil.fileExists(dir)) return false;
        
        // there is a problem with not using the clipboard here as the original
        // file could have very well been deleted before this file operation
        // is actually run, in which case, this whole method will fail.
        //
        // this right here is a band-aid fix to prevent this from blowing up
        if (!EFileUtil.allFilesExist(filesToCopy)) {
            filesToCopy.clear();
            isCutOperation = false;
            return false;
        }
        
        for (File f : filesToCopy) {
            try {
                // check if file already exists and if it does, try to increment a number after its name
                if (f.getParentFile().equals(dir)) {
                    dir = new File(dir, generateUniqueFileName(f));
                }
                
                if (f.isDirectory()) FileUtils.copyDirectory(f, dir);
                else if (dir.isDirectory()) FileUtils.copyFileToDirectory(f, dir);
                else FileUtils.copyFile(f, dir);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return true;
    }
    
    /**
     * Performs the file cut operation by moving the current working file
     * into the given directory.
     * 
     * @param dir The directory to copy into
     * @return True if successfully copied
     */
    public static boolean performCut(File dir) {
        // make sure there is even a file to copy
        if (filesToCopy == null) return false;
        // make sure the directory we are copying into actually exists
        if (!EFileUtil.fileExists(dir)) return false;
        
        // there is a problem with not using the clipboard here as the original
        // file could have very well been deleted before this file operation
        // is actually run, in which case, this whole method will fail.
        //
        // this right here is a band-aid fix to prevent this from blowing up
        if (!EFileUtil.allFilesExist(filesToCopy)) {
            filesToCopy.clear();
            isCutOperation = false;
            return false;
        }
        
        for (File f : filesToCopy) {
            try {
                // check for same destination error
                if (f.getParentFile().equals(dir)) continue;
                
                if (f.isDirectory()) FileUtils.moveDirectoryToDirectory(f, dir, false);
                else FileUtils.moveFileToDirectory(f, dir, false);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        // reset these values so as to not move many times :)
        filesToCopy.clear();
        isCutOperation = false;
        
        return true;
    }
    
    public static File createFileInDir(File directory, String fileName) throws IOException {
        return createFileInDir(directory, new File(fileName));
    }
    public static File createFileInDir(File directory, File file) throws IOException {
        if (!EFileUtil.fileExists(directory)) return null;
        String name = generateUniqueFileName(directory, file);
        File toCreate = new File(directory, name);
        Path created = Files.createFile(toCreate.toPath());
        return (created != null) ? created.toFile() : null;
    }
    
    public static File createDirectoryInDir(File directory, String fileName) throws IOException {
        return createDirectoryInDir(directory, new File(fileName));
    }
    public static File createDirectoryInDir(File directory, File dir) throws IOException {
        if (!EFileUtil.fileExists(directory)) return null;
        String name = generateUniqueFileName(directory, dir);
        File toCreate = new File(directory, name);
        Path created = Files.createDirectory(toCreate.toPath());
        return (created != null) ? created.toFile() : null;
    }
    
    public static String generateUniqueFileName(File fileToCreate) {
        return generateUniqueFileName(fileToCreate.getParentFile(), fileToCreate);
    }
    public static String generateUniqueFileName(File currentDir, String fileToCreate) {
        return generateUniqueFileName(currentDir, new File(fileToCreate));
    }
    public static String generateUniqueFileName(File currentDir, File fileToCreate) {
        if (fileToCreate == null) return null;
        
        String extension = EFileUtil.getFileExtension(fileToCreate);
        File parentDir = currentDir;
        
        // get the name of the file
        String destName = fileToCreate.getName();
        
        // strip extension
        for (int i = destName.length() - 1; i >= 0; i--) {
            char c = destName.charAt(i);
            if (c == '.') {
                // check if we can even strip off the extension
                if (i - 1 >= 0) destName = destName.substring(0, i);
                break;
            }
        }
        
        // check if there are any numbers on the end already -- trim the '_####' off
        boolean hasDigit = false;
        
        for (int i = destName.length() - 1; i >= 0; i--) {
            char c = destName.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            else if (c == '_' && hasDigit) {
                // strip name to right before number underscore
                destName = destName.substring(0, i);
                break;
            }
        }
        
        int endNumber = 1;
        File testFile = null;
        
        String destFileName = destName;
        if (hasDigit) { destFileName += "_" + (endNumber++); }
        destFileName += extension;
        
        // keep incrementing the end number until
        do {
            testFile = new File(parentDir, destFileName);
            
            if (!EFileUtil.fileExists(testFile)) break;
            
            destFileName = destName;
            destFileName += "_" + (endNumber++);
            destFileName += extension;
        }
        while (true);
        
        return testFile.getName();
    }
    
    //======================
    // File Opening Helpers
    //======================
    
    /** {@link DesktopUtil#openFile(String) openFile} */
    public static <T extends IWindowObject> T openFile(String fileName) {
        return DesktopUtil.openFile(new File(fileName), false);
    }
    
    /** {@link DesktopUtil#openFileAtMouse(String) openFileAtMouse} */
    public static <T extends IWindowObject> T openFileAtMouse(String fileName) {
        return DesktopUtil.openFileAtMouse(new File(fileName));
    }
    
    /** {@link DesktopUtil#openFile(File) openFile} */
    public static <T extends IWindowParent> T openFile(File file) {
        return DesktopUtil.openFile(file, false);
    }
    
    /** {@link DesktopUtil#openFileExplorer(File) openFileAtMouse} */
    public static <T extends IWindowParent> T openFileAtMouse(File file) {
        return DesktopUtil.openFileAtMouse(file);
    }
    
    /** {@link DesktopUtil#openFile(File, boolean) openFile} */
    public static <T extends IWindowParent> T openFile(File file, boolean inSystemOS) {
        return DesktopUtil.openFile(file, inSystemOS);
    }
    
    /** {@link DesktopUtil#openTerminalWindow() openTerminalWindow} */
    public static ETerminalWindow openTerminal() {
        return DesktopUtil.openTerminalWindow();
    }
    
    public static ETerminalWindow openTerminal(String command, String... arguments) {
        return openTerminal(command, EList.of(arguments));
    }
    
    public static ETerminalWindow openTerminal(String command, EList<String> arguments) {
        var term = DesktopUtil.openTerminalWindow();
        term.runCommand(command, arguments);
        return term;
    }
    
    /** {@link DesktopUtil#openFileExplorer() openFileExplorer} */
    public static FileExplorerWindow openFileExplorer() {
        return DesktopUtil.openFileExplorer();
    }
    
    /** {@link DesktopUtil#openFileExplorer(File) openFileExplorer} */
    public static FileExplorerWindow openFileExplorer(File dir) {
       return DesktopUtil.openFileExplorer(dir);
    }
    
    public static void reloadFileExplorers() {
        getInstance().reloadAllWindowInstances(FileExplorerWindow.class);
    }
    
}

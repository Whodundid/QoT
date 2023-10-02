package envision.engine.windows.developerDesktop.util;

import java.awt.Desktop;
import java.io.File;

import org.apache.commons.io.FileUtils;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.bundledWindows.TextureDisplayer;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.windowObjects.utilityObjects.ErrorDialogBox;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.file.FileOpener;

/**
 * A utility class which contains useful functions for interfacing with the
 * Envision Developer Desktop.
 */
public class DesktopUtil {
    
    //==============
    // Constructors
    //==============
    
    /** Hidden by default. */
    private DesktopUtil() {}
    
    //=========================
    // General Utility Methods
    //=========================
    
    /**
     * Displays the given screen on the developer desktop.
     * 
     * @param <T> A type of window parent
     * @param windowIn The location of where to display the file explorer window on screen
     * 
     * @return The opened window
     */
    public static <T extends IWindowParent> T openWindow(T windowIn) {
        return Envision.getDeveloperDesktop().displayWindow(windowIn);
    }
    
    /**
     * Displays the given screen on the developer desktop at the given position.
     * 
     * @param <T> A type of window parent
     * @param windowIn The window to open
     * @param position 
     * 
     * @return The opened window
     */
    public static <T extends IWindowParent> T openWindow(T windowIn, ObjectPosition position) {
        return Envision.getDeveloperDesktop().displayWindow(windowIn, position);
    }
    
    //==================
    // Terminal Methods
    //==================
    
    /**
     * Opens a new terminal window on the desktop.
     * 
     * @param <T> A type of IWindowObject
     * @return The terminal window that was opened
     */
    public static ETerminalWindow openTerminalWindow() {
        return openTerminalWindow(ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    /**
     * Opens a new terminal window on the desktop.
     * 
     * @param <T> A type of IWindowObject
     * @param dir The location of where to display the terminal window on screen
     * 
     * @return The terminal window that was opened
     */
    public static ETerminalWindow openTerminalWindow(ObjectPosition position) {
        return openWindow(new ETerminalWindow(), position);
    }
    
    //=======================
    // File Explorer Methods
    //=======================
    
    /**
     * Opens a new file explorer window at the default user dir.
     * 
     * @param <T> A type of IWindowObject
     * @return The IWindowObject that the given file was opened in
     */
    public static FileExplorerWindow openFileExplorer() {
        return openFileExplorer(new File(System.getProperty("user.dir")));
    }
    
    /**
     * Opens a new file explorer window at the default user dir at the
     * cursor's center.
     * 
     * @param <T> A type of IWindowObject
     * @return The IWindowObject that the given file was opened in
     */
    public static FileExplorerWindow openFileExplorerAtMouse() {
        return openFileExplorer(new File(System.getProperty("user.dir")));
    }
    
    /**
     * Opens the given directory/file in a new file explorer window either
     * at the center of the screen or indented off of an already existing
     * file explorer if there is one.
     * 
     * @param <T> A type of IWindowObject
     * @param dir The directory (or file) to open in a file explorer window
     * 
     * @return The opened file explorer window
     */
    public static FileExplorerWindow openFileExplorer(File dir) {
        return openWindow(new FileExplorerWindow(dir), ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    /**
     * Opens the given directory/file in a new file explorer window at the
     * cursor's center.
     * 
     * @param <T> A type of IWindowObject
     * @param dir The directory (or file) to open in a file explorer window
     * 
     * @return The opened file explorer window
     */
    public static FileExplorerWindow openFileExplorerAtMouse(File dir) {
        return openWindow(new FileExplorerWindow(dir), ObjectPosition.CURSOR_CENTER);
    }
    
    /**
     * Opens the given directory/file in a new file explorer window.
     * 
     * @param <T> A type of IWindowObject
     * @param dir The directory (or file) to open in a file explorer window
     * @param position The location of where to display the file explorer window on screen
     * 
     * @return The opened file explorer window
     */
    public static FileExplorerWindow openFileExplorer(File dir, ObjectPosition position) {
        return openWindow(new FileExplorerWindow(dir), position);
    }
    
    //========================
    // Picture Viewer Methods
    //========================
    
    /**
     * Opens the given file in a text editor window.
     * 
     * @param file The file to open
     * @return The opened text editor window
     */
    public static TextureDisplayer openInPictureViewer(File file) {
        return openInPictureViewer(file, ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    /**
     * Opens the given file in a text editor window at the cursor's center.
     * 
     * @param file The file to open
     * @return The opened text editor window
     */
    public static TextureDisplayer openInPictureViewerAtMouse(File file) {
        return openInPictureViewer(file, ObjectPosition.CURSOR_CENTER);
    }
    
    /**
     * Opens the given file in a text editor window at the given position on screen.
     * 
     * @param file The file to open
     * @param position The location of where to display the file explorer window on screen
     * 
     * @return The opened text editor window
     */
    public static TextureDisplayer openInPictureViewer(File file, ObjectPosition position) {
        return openWindow(new TextureDisplayer(file), position);
    }
    
    //====================
    // TextEditor Methods
    //====================
    
    /**
     * Opens the given file in a text editor window.
     * 
     * @param file The file to open
     * @return The opened text editor window
     */
    public static TextEditorWindow openInTextEditor(File file) {
        return openInTextEditor(file, ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    /**
     * Opens the given file in a text editor window at the cursor's center.
     * 
     * @param file The file to open
     * @return The opened text editor window
     */
    public static TextEditorWindow openInTextEditorAtMouse(File file) {
        return openInTextEditor(file, ObjectPosition.CURSOR_CENTER);
    }
    
    /**
     * Opens the given file in a text editor window at the given position on screen.
     * 
     * @param file The file to open
     * @param position The location of where to display the file explorer window on screen
     * 
     * @return The opened text editor window
     */
    public static TextEditorWindow openInTextEditor(File file, ObjectPosition position) {
        return openWindow(new TextEditorWindow(file), position);
    }
    
    //======================
    // File Utility Methods
    //======================
    
    /**
     * Attempts to open a file of the path as the given string inside of
     * the Envision Developer Desktop. In the event that the given file
     * type is not supported, the file will be opened in the system's OS
     * instead.
     * 
     * @param fileName the name of a file to open
     */
    public static <T extends IWindowParent> T openFile(String fileName) {
        return openFile(new File(fileName), false);
    }
    
    /**
     * Attempts to open a file of the path as the given string inside of
     * the Envision Developer Desktop. In the event that the given file
     * type is not supported, the file will be opened in the system's OS
     * instead.
     * 
     * @param fileName the name of a file to open
     */
    public static <T extends IWindowParent> T openFileAtMouse(String fileName) {
        return openFile(new File(fileName), false, ObjectPosition.CURSOR_CENTER);
    }
    
    /**
     * Attempts to open the given file inside of the Envision Developer
     * Desktop. In the event that the given file type is not supported, the
     * file will be opened in the system's OS instead.
     * 
     * @param file The file to open
     */
    public static <T extends IWindowParent> T openFile(File file) {
        return openFile(file, false);
    }
    
    /**
     * Attempts to open the given file inside of the Envision Developer
     * Desktop. In the event that the given file type is not supported, the
     * file will be opened in the system's OS instead.
     * 
     * @param file The file to open
     */
    public static <T extends IWindowParent> T openFileAtMouse(File file) {
        return openFile(file, false, ObjectPosition.CURSOR_CENTER);
    }
    
    /**
     * Attempts to open the given file either in the Envision Developer
     * Desktop or in the user's system OS.
     * <p>
     * If 'inSystemOS' is set to false, the given file will try to be
     * opened within the Envision Developer Desktop if the file type is
     * supported. In the event that the file's extension type is not
     * recognized or supported, Envision will attempt to open the given
     * file inside of the native operating system instead.
     * 
     * @param file       The file to open
     * @param inSystemOS Specifies if the file should be opened in the
     *                   system's OS rather than in the Envision Developer
     *                   Desktop
     * 
     * @return If opened in Envision, this is the window object that the
     *         file was opened in
     */
    public static <T extends IWindowParent> T openFile(File file, boolean inSystemOS) {
        return openFile(file, inSystemOS, ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    /**
     * Attempts to open the given file either in the Envision Developer
     * Desktop or in the user's system OS.
     * <p>
     * If 'inSystemOS' is set to false, the given file will try to be
     * opened within the Envision Developer Desktop if the file type is
     * supported. In the event that the file's extension type is not
     * recognized or supported, Envision will attempt to open the given
     * file inside of the native operating system instead.
     * 
     * @param file       The file to open
     * @param inSystemOS Specifies if the file should be opened in the
     *                   system's OS rather than in the Envision Developer
     *                   Desktop
     * 
     * @return If opened in Envision, this is the window object that the
     *         file was opened in
     */
    public static <T extends IWindowParent> T openFile(File file, boolean inSystemOS, ObjectPosition position) {
        // if opening in system OS, open it using the system file opener
        if (inSystemOS) {
            FileOpener.openFile(file);
            return null;
        }
        
        // if the file is a directory, open it in the file explorer
        if (file.isDirectory()) {
            return (T) openFileExplorer(file, position);
        }
        
        // grab the file extension to see if we can even 
        String extension = EFileUtil.getFileExtension(file);
        
        switch (extension) {
        // text based files
        case ".nvis":
        case ".twld":
        case ".txt":
        case ".ini":
        case ".json":
        case ".log":
        case ".xml":
        case ".cfg":
        case ".settings":
        case ".java":
        case ".cs":
        case ".cpp":
        case ".d":
        case ".c":
        case ".py":
        case ".yml":
        case ".yaml":
        case "": // assume that empty extensions are potentially text files
            return (T) openInTextEditor(file, position);
        
        // image based files
        case ".png":
        case ".bmp":
        case ".jpg":
        case ".gif":
        case ".tga":
            return (T) openInPictureViewer(file, position);
        
        // for non-supported file types, open in native OS
        default:
            FileOpener.openFile(file);
        }
        
        return null;
    }
    
    //==================
    // Terminal Helpers
    //==================
    
    public static void openInTerminal(File file) {
        if (file == null) return;
        
        var term = new ETerminalWindow();
        Envision.getDeveloperDesktop().displayWindow(term, ObjectPosition.CURSOR_CENTER);
        
        if (!file.exists()) {
            term.error("File Error!");
            term.error("The file/directory: '", file, "' does not exist!");
            return;
        }
        
        File dirToOpen;
        
        if (file.isDirectory()) dirToOpen = file;
        else dirToOpen = file.getParentFile();
        
        term.setDir(dirToOpen);
    }
    
    //=======================
    // File Deletion Helpers
    //=======================
    
    /**
     * Recycles the given file or directory into the host filesystem's
     * recycle bin.
     * 
     * @param filePreview The file to recycle
     * 
     * @return True if successfully recycled
     */
    public static boolean recycleFile(File file) {
        try {
            var desktop = Desktop.getDesktop();
            
            if (desktop.isSupported(Desktop.Action.MOVE_TO_TRASH)) {
                if (!Desktop.getDesktop().moveToTrash(file)) {
                    ErrorDialogBox.showDialog("Error! Failed to recycle file");
                    return false;
                }
                return true;
            }
            else {
                ErrorDialogBox.showDialog("Error! File cannot be recycled: Not supported action");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorDialogBox.showDialog("Error! Failed to recycle file: " + e);
        }
        
        return false;
    }
    
    /**
     * Recycles the all given files and (or) directories into the host
     * filesystem's recycle bin.
     * 
     * @param filePreview The file(s) to recycle
     * 
     * @return True if all files were successfully recycled
     */
    public static boolean recycleMultipleFiles(EList<File> files) {
        File currentFile = null;
        
        try {
            var desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.MOVE_TO_TRASH)) {
                ErrorDialogBox.showDialog("Error! File cannot be recycled: Action not supporred!");
                return false;
            }
            
            for (File f : files) {
                currentFile = f;
                
                if (!desktop.moveToTrash(currentFile)) {
                    ErrorDialogBox.showDialog("Error! Failed to recycle file: " + currentFile);
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorDialogBox.showDialog("Error! Failed to recycle file: " + currentFile);
            return false;
        }
        
        return true;
    }
    
    /**
     * Deletes the file or directory from the host filesystem.
     * 
     * @param filePreview The file to delete
     * 
     * @return True if successfully deleted
     */
    public static boolean deleteFile(File file) {
        if (file == null) return false;
        
        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
                return true;
            }
            else if (file.delete()) return true;
            else ErrorDialogBox.showDialog("Error! Failed to delete file");
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorDialogBox.showDialog("Error! Failed to delete file: " + e);
        }
        
        return false;
    }
    
    /**
     * Deletes the all given files and (or) directories from the host
     * filesystem.
     * 
     * @param filePreview The file(s) to delete
     * 
     * @return True if all files were successfully deleted
     */
    public static boolean deleteMultipleFiles(EList<File> files) {
        File currentFile = null;
        
        try {
            for (File f : files) {
                currentFile = f;
                
                if (currentFile.isDirectory()) FileUtils.deleteDirectory(currentFile);
                else if (!currentFile.delete()) {
                    ErrorDialogBox.showDialog("Error! Failed to delete file");
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorDialogBox.showDialog("Error! Failed to recycle file: " + currentFile);
        }
        
        return true;
    }
    
}

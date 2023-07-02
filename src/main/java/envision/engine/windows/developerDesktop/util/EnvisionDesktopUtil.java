package envision.engine.windows.developerDesktop.util;

import java.awt.Desktop;
import java.io.File;

import org.apache.commons.io.FileUtils;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.ErrorDialogBox;
import envision.engine.windows.bundledWindows.TextEditorWindow;
import envision.engine.windows.bundledWindows.TextureDisplayer;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.file.FileOpener;

/**
 * A utility class which contains useful functions for interfacing with the
 * Envision Developer Desktop.
 */
public class EnvisionDesktopUtil {
    
    //==============
    // Constructors
    //==============
    
    /** Hidden by default. */
    private EnvisionDesktopUtil() {}
    
    //======================
    // File Utility Methods
    //======================
    
    /**
     * Attempts to open the given file inside of the Envision Developer
     * Desktop. In the event that the given file type is not supported, the
     * file will be opened in the system's OS instead.
     * 
     * @param file The file to open
     */
    public static void openFile(File file) {
        openFile(file, false);
    }
    
    /**
     * Attempts to open the given file either in the Envision Developer Desktop
     * or in the user's system OS.
     * <p>
     * If 'inSystemOS' is set to false, the given file will try to be opened
     * within the Envision Developer Desktop if the file type is supported. In
     * the event that the file's extension type is not recognized or supported,
     * Envision will attempt to open the given file inside of the native
     * operating system instead.
     * 
     * @param file       The file to open
     * @param inSystemOS Specifies if the file should be opened in the system's
     *                   OS rather than in the Envision Developer Desktop
     *                   
     * @return If opened in Envision, this is the window object that the file
     *         was opened in
     */
    public static <T extends IWindowObject> T openFile(File file, boolean inSystemOS) {
        // if opening in system OS, open it using the system file opener
        if (inSystemOS) {
            FileOpener.openFile(file);
            return null;
        }
        
        // if the file is a directory, open it in the file explorer
        if (file.isDirectory()) {
            return (T) Envision.getTopScreen().displayWindow(new FileExplorerWindow(file), ObjectPosition.EXISTING_OBJECT_INDENT);
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
            return (T) Envision.getTopScreen().displayWindow(new TextEditorWindow(file), ObjectPosition.EXISTING_OBJECT_INDENT);
        
        // image based files
        case ".png":
        case ".bmp":
        case ".jpg":
        case ".gif":
        case ".tga":
            return (T) Envision.getTopScreen().displayWindow(new TextureDisplayer(file), ObjectPosition.EXISTING_OBJECT_INDENT);
        
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
        Envision.getTopScreen().displayWindow(term, ObjectPosition.CURSOR_CENTER);
        
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
     * Recycles the given file or directory into the host filesystem's recycle bin.
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

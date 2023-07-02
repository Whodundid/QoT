package envision.engine.windows.developerDesktop;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut;
import envision.engine.windows.windowTypes.WindowObject;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import qot.settings.QoTSettings;

public class EnvisionDeveloperDesktop extends WindowObject {
    
    /** The directory for where content for this desktop is located. */
    public static final File DESKTOP_DIR = new File(QoTSettings.getLocalGameDir(), "desktop");
    /** All shortcuts actively displayed on the desktop overlay. */
    private static final EList<DesktopShortcut> shortcuts = EList.newList();
    
    // setup the desktop directory upfront
    static {
        if (!DESKTOP_DIR.exists()) DESKTOP_DIR.mkdirs();
    }
    
    public void addShortcut(DesktopShortcut shortcut) {
        
    }
    
    public void removeShortcut(DesktopShortcut shortcut) {
        
    }
    
    /** Returns the list of all shortcuts actively on the desktop. */
    public EList<DesktopShortcut> getShortcuts() {
        return shortcuts;
    }
    
    //======================
    // File copy operations
    //======================
    
    private static File fileToCopy;
    private static boolean isCutOperation;
    
    public static void setFileToCopy(File fileToCopyIn) {
        fileToCopy = fileToCopyIn;
        isCutOperation = false;
    }
    
    public static void setFileToCut(File fileToCutIn) {
        fileToCopy = fileToCutIn;
        isCutOperation = true;
    }
    
    public static void completeCutOperation() {
        fileToCopy = null;
        isCutOperation = false;
    }
    
    public static File getWorkingFile() { return fileToCopy; }
    public static boolean isCutOperation() { return isCutOperation; }
    /** Returns true if the given file matches the current working file to be cut. */
    public static boolean checkFileForCut(File fileIn) {
        return fileIn != null && fileIn.equals(fileToCopy) && isCutOperation;
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
        if (fileToCopy == null) return false;
        // make sure the directory we are copying into actually exists
        if (!EFileUtil.fileExists(dir)) return false;
        
        // there is a problem with not using the clipboard here as the original
        // file could have very well been deleted before this file operation
        // is actually run, in which case, this whole method will fail.
        //
        // this right here is a band-aid fix to prevent this from blowing up
        if (!EFileUtil.fileExists(fileToCopy)) {
            fileToCopy = null;
            isCutOperation = false;
            return false;
        }
        
        try {
            // check if file already exists and if it does, try to increment a number after its name
            if (fileToCopy.getParentFile().equals(dir)) {
                dir = new File(dir, generateFileCopyName(fileToCopy));
            }
            
            if (fileToCopy.isDirectory()) FileUtils.copyDirectory(fileToCopy, dir);
            else FileUtils.copyFile(fileToCopy, dir);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static String generateFileCopyName(File destination) {
        if (destination == null) return null;
        
        String extension = EFileUtil.getFileExtension(destination);
        File parentDir = destination.getParentFile();
        
        // get the name of the file
        String destName = destination.getName();
        
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
                continue;
            }
            else if (c == '_' && hasDigit) {
                // strip name to right before number underscore
                destName = destName.substring(0, i);
                break;
            }
        }
        
        int endNumber = 1;
        File testFile = null;
        // keep incrementing the end number until 
        while (EFileUtil.fileExists(testFile = new File(parentDir, destName + "_" + (endNumber++) + extension)));
        
        return testFile.getName();
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
        if (fileToCopy == null) return false;
        // make sure the directory we are copying into actually exists
        if (!EFileUtil.fileExists(dir)) return false;
        
        // there is a problem with not using the clipboard here as the original
        // file could have very well been deleted before this file operation
        // is actually run, in which case, this whole method will fail.
        //
        // this right here is a band-aid fix to prevent this from blowing up
        if (!EFileUtil.fileExists(fileToCopy)) {
            fileToCopy = null;
            isCutOperation = false;
            return false;
        }
        
        try {
            FileUtils.moveFileToDirectory(fileToCopy, dir, false);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        // reset these values so as to not move many times :)
        fileToCopy = null;
        isCutOperation = false;
        
        return true;
    }
    
}

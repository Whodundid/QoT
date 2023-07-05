package envision.engine.windows.developerDesktop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import envision.Envision;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import qot.settings.QoTSettings;

public class DeveloperDesktop extends WindowObject {
    
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
    
    public static void openFileExplorer() { openFileExplorer(new File(System.getProperty("user.dir"))); }
    public static void openFileExplorer(File dir) {
        Envision.getTopScreen().displayWindow(new FileExplorerWindow(dir), ObjectPosition.EXISTING_OBJECT_INDENT);
    }
    
    public static void reloadFileExplorers() {
        Envision.getTopScreen().reloadAllWindowInstances(FileExplorerWindow.class);
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
                continue;
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
    
    public static boolean createFileInDir(File directory, String fileName) throws IOException {
        return createFileInDir(directory, new File(fileName));
    }
    public static boolean createFileInDir(File directory, File file) throws IOException {
        if (!EFileUtil.fileExists(directory)) return false;
        String name = generateUniqueFileName(directory, file);
        File toCreate = new File(directory, name);
        return Files.createFile(toCreate.toPath()) != null;
    }
    
    public static boolean createDirectoryInDir(File directory, String fileName) throws IOException {
        return createDirectoryInDir(directory, new File(fileName));
    }
    public static boolean createDirectoryInDir(File directory, File dir) throws IOException {
        if (!EFileUtil.fileExists(directory)) return false;
        String name = generateUniqueFileName(directory, dir);
        File toCreate = new File(directory, name);
        return Files.createDirectory(toCreate.toPath()) != null;
    }
    
    public static String generateUniqueFileName(File fileToCreate) {
        return generateUniqueFileName(fileToCreate.getParentFile(), fileToCreate);
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
    
}

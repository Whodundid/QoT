package envision.engine.windows.developerDesktop.shortcuts;

import java.io.File;

import envision.engine.terminal.terminalUtil.FileType;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import eutil.file.EFileUtil;
import eutil.file.FileOpener;
import eutil.strings.EStringBuilder;
import qot.assets.textures.window.WindowTextures;

public class DesktopShortcut_File extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private File theFile;
    
    //==============
    // Constructors
    //==============
    
    // for use in parsing
    public DesktopShortcut_File() {
        super("New File", ShortcutType.FILE);
    }
    
    public DesktopShortcut_File(double x, double y, File fileIn) {
        super(fileIn.getName(), x, y, ShortcutType.FILE);
        theFile = fileIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        if (theFile == null) return;
        
        DesktopUtil.openFile(theFile);
    }
    
    @Override
    public void openShortcutRCM() {
        createBaseShortcutRCM();
        
        String command = (theFile.isDirectory()) ? "Open Directory" : "Open File";
        shortcutRCM.addOption(command, WindowTextures.file_up, this::onDoubleClick);
        shortcutRCM.addOption("Open In OS", () -> FileOpener.openFile(theFile));
        shortcutRCM.addOption("Copy", this::copyShortcut);
        shortcutRCM.addOption("Cut", this::cutShortcut);
        shortcutRCM.addOption("Delete", WindowTextures.red_x, this::deleteShortcut);
        shortcutRCM.addOption("Rename", this::renameShortcut);
        shortcutRCM.addOption("Properties", this::openProperties);
        
        shortcutRCM.showOnCurrent();
    }
    
    @Override
    public void generateSaveString(EStringBuilder sb) {
        generateBaseSaveString(sb);
        sb.println("target: ", theFile);
        sb.decrementTabCount();
    }
    
    public void setFile(File fileIn) {
        theFile = fileIn;
        
        if (theFile != null) {
            this.setName(theFile.getName());
            
            FileType fileType = FileType.getFileType(theFile);
            this.setIcon(fileType.getFileTexture());            
        }
    }
    
    @Override
    public void deleteShortcut() {
        DeveloperDesktop.removeShortcut(this);
        
        if (theFile == null) return;
        if (theFile.isDirectory()) EFileUtil.deleteDirectory(theFile);
        else theFile.delete();
    }
    
}

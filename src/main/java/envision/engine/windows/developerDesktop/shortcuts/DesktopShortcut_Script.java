package envision.engine.windows.developerDesktop.shortcuts;

import envision.engine.scripting.EnvisionScript;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import qot.assets.textures.window.WindowTextures;

public class DesktopShortcut_Script extends DesktopShortcut {
    
    //========
    // Fields
    //========
    
    private EnvisionScript theScript;
    private EList<String> args;
    
    //==============
    // Constructors
    //==============
    
    public DesktopShortcut_Script(String shortcutName) {
        super(shortcutName, ShortcutType.SCRIPT);
    }
    
    public DesktopShortcut_Script(int x, int y, EnvisionScript scriptIn, EList<String> argsIn) {
        this("" + scriptIn, x, y, scriptIn, argsIn);
    }
    
    public DesktopShortcut_Script(String shortcutName, int x, int y, EnvisionScript scriptIn, EList<String> argsIn) {
        super(shortcutName, x, y, ShortcutType.SCRIPT);
        theScript = scriptIn;
        args = argsIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void openShortcut() {
        
    }
    
    @Override
    public void openShortcutRCM() {
        createBaseShortcutRCM();
        
        shortcutRCM.addOption("Run Script", WindowTextures.file_up, this::onDoubleClick);
        shortcutRCM.addOption("Edit", () -> DesktopUtil.openInTextEditor(theScript.getFile()));
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
        sb.println("script: ", theScript);
        sb.decrementTabCount();
    }
    
}

package envision.engine.windows.developerDesktop.config;

import java.io.File;
import java.io.IOException;

import envision.engine.windows.developerDesktop.DeveloperDesktop;

/**
 * A wrapper for a file that keeps track of all shortcuts/programs/items on
 * the desktop along with their positions.
 * <p>
 * When any action modifies the desktop, the corresponding content file
 * should be updated to reflect these changes across engine runs.
 * 
 * @author Hunter Bragg
 */
public class DesktopContentTracker {
    
    public static final File DESKTOP_CONFIG_FILE = new File(DeveloperDesktop.DESKTOP_DIR, "desktop_config.ini");
    
    static {
        if (!DESKTOP_CONFIG_FILE.exists()) {
            try {
                DESKTOP_CONFIG_FILE.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void loadConfigFile() {
        
    }
    
    public void saveConfigFile() {
        
    }
    
    //=========
    // Parsers
    //=========
    
}

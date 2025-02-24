package envision.engine.loader.built.game;

import java.io.File;

import envision.engine.internal.kernel.developerDesktop.DeveloperDesktop;
import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.ScriptResourceDTO;

public class EnvisionScript implements IEngineResource {
    
    // This is a temporary placeholder
    private File theFile = DeveloperDesktop.DESKTOP_DIR;
    
    public File getFile() { return theFile; }

    @Override
    public ScriptResourceDTO toDto() {
        return null;
    }
    
}

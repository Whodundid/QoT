package envision.debug.testGame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import envision.engine.settings.config.ConfigSetting;
import envision.game.EnvisionGameSettings;
import eutil.datatypes.util.EList;

public class TestGameSettings extends EnvisionGameSettings {

    private final EList<ConfigSetting<?>> settings = EList.newList();
    
    private File resources;
    private File dir;
    private File saved;
    
    @Override public File getInstallationDirectory() { return dir; }
    @Override public File getResourcesDirectory() { return resources; }
    @Override public File getSavedGamesDirectory() { return saved; }

    @Override public EList<ConfigSetting<?>> getConfigSettings() { return settings; }
    
    @Override
    protected void initializeGameDirectories(File installDir, boolean useInternalResources) {
        dir = installDir;
        resources = new File(dir, "resources");
        saved = new File(dir, "saved-games");
        
        try {
            final Path dirPath = dir.toPath();
            final Path resourcesPath = resources.toPath();
            final Path savedPath = saved.toPath();
            
            if (!Files.exists(dirPath)) Files.createDirectory(dirPath);
            if (!Files.exists(resourcesPath)) Files.createDirectory(resourcesPath);
            if (!Files.exists(savedPath)) Files.createDirectory(savedPath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

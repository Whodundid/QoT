package envision.debug.testGame;

import java.io.File;

import envision.engine.settings.config.EnvisionConfigFile;

public class TestGameConfig extends EnvisionConfigFile {

    public TestGameConfig(File path, String name) { super(path, name); }

    @Override
    public boolean trySave() { return false; }

    @Override
    public boolean tryLoad() { return false; }

    @Override
    public boolean tryReset() { return false; }
    
}

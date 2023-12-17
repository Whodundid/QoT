package envision.engine.loader;

import java.io.File;

public class GameLoader {
    
    public static void loadGameFromDirectory(File directory) {
        EnvisionGameDirectory dir = new EnvisionGameDirectory(directory);
        
        dir.getGameLoadFile();
    }
    
}

package envision.engine.loader;

import java.io.File;

public class GameLoader {
    
    public static LoadedGameDirectory loadGameFromDirectory(File directory) {
        LoadedGameDirectory dir = new LoadedGameDirectory(directory);
        
        dir.getGameLoadFile();
        
        return dir;
    }
    
}

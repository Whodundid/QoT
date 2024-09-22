package envision.engine.registry.registries;

import java.io.File;

import envision.engine.loader.GameLoader;
import envision.engine.loader.LoadedGameDirectory;
import envision.engine.registry.AbstractResourceRegistry;
import envision.engine.rendering.textureSystem.GameTexture;

public class TextureRegistry extends AbstractResourceRegistry<GameTexture> {
    
    //==============
    // Constructors
    //==============
    
    public TextureRegistry() {
        super("Texture Registry");
    }
    
    //=========
    // Methods
    //=========
    
    @Override
    public void loadResources() {
        resources.clear();
        
        File dir = new File("C:/Users/Hunter/AppData/Roaming/Quest of Thyrah");
        LoadedGameDirectory gameDir = GameLoader.loadGameFromDirectory(dir);
        
        String resourceDirString = gameDir.getGameDTO().getResourcesDir();
        File resourcesDir = new File(resourceDirString);
        
        
        
        
        System.out.println(resourcesDir);
    }
    
    @Override
    public void saveResources() {
        
    }
    
}

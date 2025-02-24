package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record TextureResourceDTO(
    String name,
    String filePath,
    @Optional String minFilter,
    @Optional String magFilter
)
    implements IDataTransferObject<GameTexture>
{
    //==============
    // Constructors
    //==============
    
    /** Constructor to ensure that values are property set. */
    public TextureResourceDTO(String name, String filePath, @Optional String minFilter, @Optional String magFilter) {
        Assertions.assertNotNull(name, filePath);
        
        filePath = filePath.replace("\\", "/");
        
        this.name = name;
        this.filePath = filePath;
        this.minFilter = minFilter;
        this.magFilter = magFilter;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public GameTexture fromDto() {
        return new GameTexture(this);
    }

}

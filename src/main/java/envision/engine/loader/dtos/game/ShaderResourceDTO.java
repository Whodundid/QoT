package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.ShaderProgram;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record ShaderResourceDTO(@Optional String name, String filePath) implements IDataTransferObject<ShaderProgram> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public ShaderResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public ShaderResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(name, filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public ShaderProgram fromDto() {
        return null;
    }
    
}

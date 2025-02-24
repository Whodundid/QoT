package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.GameLevel;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record GameLevelDTO(@Optional String name, String filePath) implements IDataTransferObject<GameLevel> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public GameLevelDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public GameLevelDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public GameLevel fromDto() {
        return null;
    }
    
}

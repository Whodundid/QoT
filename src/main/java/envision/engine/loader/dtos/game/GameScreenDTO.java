package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.GameScreen;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record GameScreenDTO(@Optional String name, String filePath) implements IDataTransferObject<GameScreen> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public GameScreenDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public GameScreenDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public GameScreen fromDto() {
        return null;
    }
    
}

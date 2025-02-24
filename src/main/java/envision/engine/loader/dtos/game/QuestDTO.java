package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Quest;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record QuestDTO(@Optional String name, String filePath) implements IDataTransferObject<Quest> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public QuestDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public QuestDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Quest fromDto() {
        return null;
    }
    
}

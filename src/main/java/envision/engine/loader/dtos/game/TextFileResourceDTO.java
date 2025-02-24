package envision.engine.loader.dtos.game;

import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record TextFileResourceDTO(@Optional String name, String filePath) {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public TextFileResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public TextFileResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
}

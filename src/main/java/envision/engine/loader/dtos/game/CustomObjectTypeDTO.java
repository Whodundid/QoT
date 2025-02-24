package envision.engine.loader.dtos.game;

import java.util.HashMap;
import java.util.Map;

import envision.engine.loader.built.game.CustomObjectType;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record CustomObjectTypeDTO(String name, @Optional Map<String, Object> properties)
    implements IDataTransferObject<CustomObjectType>
{
    //==============
    // Constructors
    //==============
    
    public CustomObjectTypeDTO(String name, @Optional Map<String, Object> properties) {
        Assertions.assertNotNull(name);
        
        if (properties == null) properties = new HashMap<>();
        
        this.name = name;
        this.properties = properties;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public CustomObjectType fromDto() {
        return null;
    }
    
}

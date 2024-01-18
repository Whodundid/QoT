package envision.engine.loader.dtos;

import java.util.List;

import eutil.datatypes.util.EList;

public record WorldTileListDTO(List<WorldTileDTO> worldTileList) {
    
    //==============
    // Constructors
    //==============
    
    public WorldTileListDTO(WorldTileDTO... dtos) {
        this(List.of(dtos));
    }
    
    public WorldTileListDTO(EList<WorldTileDTO> dtos) {
        this(dtos.toArrayList());
    }
    
}

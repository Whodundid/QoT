package envision.engine.loader.dtos;

public interface IEngineResource {
    
    /** Converts the built object type to a DTO. */
    <T extends IEngineResource> IDataTransferObject<T> toDto();
    
}

package envision.engine.loader.dtos;

public interface IDataTransferObject<T extends IEngineResource> {
    
    /** Returns the name of resource represented by this DTO. */
    String name();
    
    /** Constructs an instance of the built object type from this DTO. */
    T fromDto() throws Exception;
    
}

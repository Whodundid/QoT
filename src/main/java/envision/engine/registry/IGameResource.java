package envision.engine.registry;

public interface IGameResource {
    
    /** Returns true if this resource has successfully been loaded into the engine. */
    boolean isLoaded();
    /** The type of resource that this is. */
    ResourceType getResourceType();
    
}

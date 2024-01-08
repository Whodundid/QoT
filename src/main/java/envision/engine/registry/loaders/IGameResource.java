package envision.engine.registry.loaders;

public interface IGameResource {
	
	/** The user's name for the given resources. Used for mapping purposes within the engine. */
	String getResourceName();
	/** Returns true if this resource has successfully been loaded into the engine. */
	boolean isLoaded();
	/** The type of resource that this is. */
	ResourceType getResourceType();
	
    /**
     * Used to set this resource to be loaded. Internal Method, should only be
     * called by resource loaders.
     */
    void setLoaded(boolean value);
	
}

package envisionEngine.resourceLoaders;

public interface IGameResource {
	
	/** The user's name for the given resources. Used for mapping purposes within the engine. */
	String getName();
	/** Returns true if this resource has successfully been loaded into the engine. */
	boolean isLoaded();
	/** The type of resource that this is. */
	ResourceType getResourceType();
	
}

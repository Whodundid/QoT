package envision.resourceLoaders;

import java.io.File;

public interface IResourceManager {
	
	/** Attempts to read, parse, and load data from this manager. */
	void load() throws Exception;
	/** Attempts to unload, and sometimes destroy, existing data within this manager. */
	void unload() throws Exception;
	/** Returns true if this manager has sucessfully loaded its data. */
	boolean isLoaded();
	/** Returns the file that this resource manager is responsible for. */
	File getResourceFile();
	
}

package envision.engine.loader.registry;

import java.io.File;

import envision.engine.loader.dtos.IEngineResource;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.util.EList;

public interface IResourceManager<TYPE extends IEngineResource> {
    
    /** Attempts to read, parse, and load data from this manager. */
    void loadDtos() throws Exception;
    /** Actually constructs the object represented by this DTO. */
    void buildResources() throws Exception;
    /** Attempts to save resource to host filesystem. */
    void saveDtos() throws Exception;
    /** Attempts to unload, and sometimes destroy, existing data within this manager. */
    void unloadResources() throws Exception;
    /** Reloads the assets in this resource manager. */
    boolean reload() throws Exception;
    
    /** Returns the file that this resource manager is responsible for. */
    File getResourceFile();
    /** Returns a single resource by name id from this manager. */
    TYPE getResource(String name);
    /** Returns all built resources under this manager. */
    EList<TYPE> getAllResources();
    
    /** Assigns this manager the underlying JSON file (or relavant data file) for which to parse its dtos to/from. */
    void assignResourceFile(File resourceFileIn);
    
    ResourceManagerState getCurrentManagerState();
    default Box2<String, Throwable> getFailureStateReason() { return null; }
    
}

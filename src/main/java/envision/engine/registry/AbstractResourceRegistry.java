package envision.engine.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eutil.datatypes.util.EList;

public abstract class AbstractResourceRegistry<T extends IGameResource> {
    
    protected final ConcurrentMap<String, T> resources = new ConcurrentHashMap<>();
    protected final String registryName;
    
    //==============
    // Constructors
    //==============
    
    protected AbstractResourceRegistry(String registryName) {
        this.registryName = registryName;
    }
    
    //===========
    // Abstracts
    //===========
    
    public abstract void loadResources();
    public abstract void saveResources();
    
    //=========
    // Methods
    //=========
    
    /**
     * Registers the given 'resource' under the given 'name'.
     * 
     * @param name         The name for which to internally refer to the given
     *                     resource by
     * @param textureSheet The resource to register
     * 
     * @return The old resource under the given name (if there was one)
     */
    public T registerResource(String name, T resource) {
        return resources.put(name, resource);
    }
    
    public T unregisterResource(String name) {
        return resources.remove(name);
    }
    
    public T getResource(String name) {
        return resources.get(name);
    }
    
    public boolean isResourceRegistered(String name) {
        return getResource(name) != null;
    }
    
    /**
     * @return A list of all 'names' of resources currently registered in this ResourceRegistry.
     */
    public EList<String> getAllResourceNames() {
        return EList.of(resources.keySet());
    }
    
    public void unregisterAllResources() {
        resources.clear();
    }
    
    //=========
    // Getters
    //=========
    
    public String getRegistryName() {
        return registryName;
    }
    
}

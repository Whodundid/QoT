package envision.engine.loader.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import envision.engine.loader.dtos.IDataTransferObject;
import envision.engine.loader.dtos.IEngineResource;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.util.EList;

public abstract class BasicResourceManager<TYPE extends IEngineResource, DTO extends IDataTransferObject<TYPE>> implements IResourceManager<TYPE> {
    
    //========
    // Fields
    //========
    
    protected final ObjectMapper mapper = new ObjectMapper();
    protected File resourceFile;
    
    private final EList<DTO> loadedDtos = EList.newList();
    private final Map<String, TYPE> loadedResources = new HashMap<>();
    private ResourceManagerState currentState = ResourceManagerState.NOT_INITIALIZED;
    private final Box2<String, Throwable> failureStateReason = new Box2<>();
    
    //==============
    // Constructors
    //==============
    
    protected BasicResourceManager() {
        this(null);
    }
    
    protected BasicResourceManager(File resourceFile) {
        this.resourceFile = resourceFile;
        
        tryManagerInitialization();
    }
    
    public final void tryManagerInitialization() {
        try {
            transitionToState(ResourceManagerState.INITIALIZING);
            initializeManager();
            transitionToState(ResourceManagerState.INITIALIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
            failureStateReason.set("Failed to initialize resource manager!", e);
            transitionToState(ResourceManagerState.FAILED_TO_INITIALIZE);
        }
    }
    
    //===========
    // Abstracts
    //===========
    
    protected void initializeManager() {
        // to be implemented by child classes
    }
    
    /** Actually facilitates the loading of JSON data into DTO objects in engine. */
    protected abstract List<? extends DTO> loadDtosFromFile() throws Exception;
    
    protected abstract void buildResourcesFromDTOs(EList<DTO> dtos, Map<String, TYPE> resources) throws Exception;
    
    /**
     * Called for each loaded resource that is about to be unloaded so that the
     * respective manager can take steps to actually 'clean up' after it.
     */
    protected abstract void onUnload(String name, TYPE resourceBeingUnloaded) throws Exception;
    
    //===========
    // Overrides
    //===========
    
    @Override
    public synchronized final void loadDtos() throws Exception {
        transitionToState(ResourceManagerState.LOAD_DTOS);
        
        try {
            loadedDtos.addAll(loadDtosFromFile());
            transitionToState(ResourceManagerState.DTOS_LOADED);
        }
        catch (Exception e) {
            e.printStackTrace();
            failureStateReason.set("Failed to load all DTOs from file: '" + resourceFile + "'!", e);
            transitionToState(ResourceManagerState.FAILED_LOADING_DTOS);
        }
    }
    
    @Override
    public synchronized final void buildResources() throws Exception {
        transitionToState(ResourceManagerState.BUILD_RESOURCES);
        
        try {
            buildResourcesFromDTOs(loadedDtos, loadedResources);
            transitionToState(ResourceManagerState.RESOURCES_BUILT);
        }
        catch (Exception e) {
            e.printStackTrace();
            failureStateReason.set("Failed to construct all resources from each loaded DTO: '" + resourceFile + "'!", e);
            transitionToState(ResourceManagerState.FAILED_BUILDING_RESOURCES);
        }
    }
    
    @Override
    public synchronized void saveDtos() throws Exception {
        transitionToState(ResourceManagerState.SAVE_RESOURCE_DTOS);
        
        try {
            List<IDataTransferObject<?>> dtos = new ArrayList<>();
            
            for (var r : loadedResources.entrySet()) {
                var dto = r.getValue().toDto();
                dtos.add(dto);
            }
            
            mapper.writeValue(resourceFile, dtos);
            transitionToState(ResourceManagerState.RESOURCES_BUILT);
        }
        catch (Exception e) {
            e.printStackTrace();
            failureStateReason.set("Failed to save all resource objects as DTOs to file: '" + resourceFile + "'!", e);
            transitionToState(ResourceManagerState.FAILED_SAVING_RESOURCE_DTOS);
        }
    }

    @Override
    public synchronized final void unloadResources() throws Exception {
        transitionToState(ResourceManagerState.UNLOAD_RESOURCES);
        
        try {
            for (var r : loadedResources.entrySet()) {
                onUnload(r.getKey(), r.getValue());
            }
            
            loadedDtos.clear();
            loadedResources.clear();
            transitionToState(ResourceManagerState.RESOURCES_UNLOADED);
        }
        catch (Exception e) {
            e.printStackTrace();
            failureStateReason.set("Failed to unload/destroy all resource objects for file: '" + resourceFile + "'!", e);
            transitionToState(ResourceManagerState.FAILED_UNLOADING_RESOURCES);
        }
    }
    
    @Override
    public synchronized boolean reload() {
        try {
            unloadResources();
            loadDtos();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public File getResourceFile() {
        return resourceFile;
    }

    @Override
    public synchronized TYPE getResource(String name) {
        return loadedResources.get(name);
    }

    @Override
    public synchronized EList<TYPE> getAllResources() {
        return EList.newList(loadedResources.values());
    }
    
    @Override
    public void assignResourceFile(File resourceFileIn) {
        resourceFile = resourceFileIn;
    }
    
    @Override
    public ResourceManagerState getCurrentManagerState() {
        return currentState;
    }
    
    @Override
    public Box2<String, Throwable> getFailureStateReason() {
        return failureStateReason;
    }
    
    //=========
    // Methods
    //=========
    
    public void transitionToState(ResourceManagerState state) {
        ResourceManagerState.checkStateTransition(this, state);
        currentState = state;
    }
    
}

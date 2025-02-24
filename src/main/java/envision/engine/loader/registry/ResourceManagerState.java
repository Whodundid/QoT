package envision.engine.loader.registry;

public enum ResourceManagerState {
    
    /**
     * Start state.
     * <p>
     * Transitions to: 'INITIALIZING'
     */
    NOT_INITIALIZED,
    
    /**
     * Manager is attempting to load and pair with its resource file.
     * <p>
     * Allowed transitions:
     * <li>'INITIALIZED' on initialization success
     * <li>'FAILED_TO_INITIALIZE' on initialization failure
     */
    INITIALIZING,
    
    /**
     * Manager is created and is paired with its resource file.
     * <p>
     * Allowed transitions:
     * <li>'LOAD_DTOS' to begin parsing data from the resource file
     */
    INITIALIZED,
    
    /**
     * Manager failed to initialze and is in an error state.
     * <p>
     * Allowed transitions:
     * <li>'INITIALIZING' to attempt manager initialization once more
     */
    FAILED_TO_INITIALIZE,
    
    /**
     * Manager will attempt to load DTOs the paired resource file.
     * <p>
     * Allowed transitions:
     * <li>'DTOS_LOADED' on successfully loading each DTO from the file
     * <li>'FAILED_LOADING_DTOS' if any dto fails to load or an error is thrown
     * during loading.
     */
    LOAD_DTOS,
    
    /**
     * All DTOs have been sucessuflly loaded.
     * <p>
     * Allowed transitions:
     * <li>'BUILD_RESOURCES' to iniate the construction of resource objects from
     * DTOs
     */
    DTOS_LOADED,
    
    /**
     * One or more DTOs failed to load or an error occurred during the DTO
     * loading process.
     * <p>
     * Allowed transitions:
     * <li>'LOAD_DTOS' to attempt DTO loading once more
     */
    FAILED_LOADING_DTOS,
    
    /**
     * Manager will attempt to actually construct each loaded DTO into a
     * resource object.
     * <p>
     * Allowed transitions:
     * <li>'RESOURCES_BUILT' if all DTOs were sucessfully built into objects
     * <li>'FAILED_BUILDING_RESOURCES' if one or more DTOs failed to be built
     * into objects
     */
    BUILD_RESOURCES,
    
    /**
     * All DTOs have sucessfully been built into actual objects.
     * <p>
     * Allowed transitions:
     * <li>'SAVE_RESOURCE_DTOS' allows the manager to save its built resources
     * back to its resource file as DTOs
     * <li>'UNLOAD_RESOURCES' to unload "deconstruct" all built objects in this
     * manager
     */
    RESOURCES_BUILT,
    
    /**
     * One or more DTOs failed to be built into a resource object.
     * <p>
     * Allowed transitions:
     * <li>'BUILD_RESOURCES' to attempt resource object construction once more
     */
    FAILED_BUILDING_RESOURCES,
    
    /**
     * Attempts to save each built resource object's data into the manager's
     * resource file as a list of DTOs.
     * <p>
     * Allowed transitions:
     * <li>'RESOURCES_BUILT' on successful DTO creation and saving
     * <li>'FAILED_SAVING_RESOURCE_DTOS' if one or more resource objects failed
     * to be saved as DTOs into the manager's resource file
     */
    SAVE_RESOURCE_DTOS,
    
    /**
     * One or more of this manager's built resource objects failed to be
     * converted to a DTO and saved to the manager's resource file.
     * <p>
     * Allowed transitions:
     * <li>'SAVE_RESOURCE_DTOS' attempt the saving process once again
     * <li>'RESOURCES_BUILT' don't try to attempt saving again and just return
     * to a regular built state
     */
    FAILED_SAVING_RESOURCE_DTOS,
    
    /**
     * Attempts to unload "deconstruct" all resource objects that have been
     * created in this manager.
     * <p>
     * Allowed transitions:
     * <li>'RESOURCES_UNLOADED' all resources objects have been successfully
     * unloaded
     * <li>'FAILED_UNLOADING_RESOURCES' one or more resource objects failed to
     * be unloaded
     */
    UNLOAD_RESOURCES,
    
    /**
     * All resources held and managed by this manager have been cleared and
     * potentially destroyed.
     * <p>
     * This includes:
     * <li>All DTOs
     * <li>All resource objects created from DTOs
     * <p>
     * Allowed transitions:
     * <li>'LOAD_DTOS' allows the manager to reparse its resource file to load
     * DTOs
     */
    RESOURCES_UNLOADED,
    
    /**
     * One or more resource objects in this manager failed to be unloaded.
     * <p>
     * Allowed transitions:
     * <li>NONE -- this is a hard fail state and should result in the engine
     * shutting down to prevent unpredictable behavior or resource leaks.
     */
    FAILED_UNLOADING_RESOURCES;
    
    //================
    // Static Methods
    //================
    
    /**
     * Checks for valid state transitions before allowing the manager to move state.
     * 
     * @param manager The manager attempting to transition to a new state
     * @param stateToTransitionTo The state to transition to
     */
    public static void checkStateTransition(IResourceManager<?> manager, ResourceManagerState stateToTransitionTo) {
        var curState = manager.getCurrentManagerState();
        
        switch (curState) {
        case NOT_INITIALIZED:
            if (stateToTransitionTo == INITIALIZING)
                return;
            break;
        case INITIALIZING:
            if (stateToTransitionTo == INITIALIZED || stateToTransitionTo == FAILED_TO_INITIALIZE)
                return;
            break;
        case FAILED_TO_INITIALIZE:
            if (stateToTransitionTo == INITIALIZING)
                return;
            break;
        case INITIALIZED:
            if (stateToTransitionTo == LOAD_DTOS)
                return;
            break;
        case LOAD_DTOS:
            if (stateToTransitionTo == DTOS_LOADED || stateToTransitionTo == FAILED_LOADING_DTOS)
                return;
            break;
        case FAILED_LOADING_DTOS:
            if (stateToTransitionTo == LOAD_DTOS)
                return;
            break;
        case DTOS_LOADED:
            if (stateToTransitionTo == BUILD_RESOURCES)
                return;
            break;
        case BUILD_RESOURCES:
            if (stateToTransitionTo == RESOURCES_BUILT || stateToTransitionTo == FAILED_BUILDING_RESOURCES)
                return;
            break;
        case FAILED_BUILDING_RESOURCES:
            if (stateToTransitionTo == BUILD_RESOURCES)
                return;
            break;
        case RESOURCES_BUILT:
            if (stateToTransitionTo == SAVE_RESOURCE_DTOS || stateToTransitionTo == UNLOAD_RESOURCES)
                return;
            break;
        case SAVE_RESOURCE_DTOS:
            if (stateToTransitionTo == RESOURCES_BUILT || stateToTransitionTo == FAILED_SAVING_RESOURCE_DTOS)
                return;
            break;
        case FAILED_SAVING_RESOURCE_DTOS:
            if (stateToTransitionTo == SAVE_RESOURCE_DTOS || stateToTransitionTo == RESOURCES_BUILT)
                return;
            break;
        case UNLOAD_RESOURCES:
            if (stateToTransitionTo == RESOURCES_UNLOADED || stateToTransitionTo == FAILED_UNLOADING_RESOURCES)
                return;
            break;
        case RESOURCES_UNLOADED:
            if (stateToTransitionTo == LOAD_DTOS)
                return;
            break;
            
        case FAILED_UNLOADING_RESOURCES:
        default:
            // fall through and fail
        }
        
        throw invalidStateError(curState, stateToTransitionTo);
    }
    
    private static IllegalStateException invalidStateError(ResourceManagerState curState, ResourceManagerState stateToTransitionTo) {
        return new IllegalStateException("Cannot transition to state: '" + stateToTransitionTo + "' from '" + curState + "'!");
    }
    
}

package envision.engine.loader.built.game;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.EntityComponentDTO;
import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;

public abstract class EntityComponent implements IEngineResource {
    
    //========
    // Fields
    //========
    
    public final ComponentBasedObject theObject;
    public final String componentName;
    public final String componentID;
    public final boolean respondsToGameTick;
    public final boolean respondsToRenderTick;
    public final Map<String, Method> functions = new HashMap<>();    
    //==============
    // Constructors
    //==============
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn, ComponentType typeIn) {
        this(theEntityIn, typeIn.text, false, false);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn, String nameIn) {
        this(theEntityIn, nameIn, UUID.randomUUID().toString(), false, false);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn, ComponentType typeIn, String idIn) {
        this(theEntityIn, typeIn.text, idIn, false, false);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn, String nameIn, String idIn) {
        this(theEntityIn, nameIn, idIn, false, false);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn,
                              ComponentType typeIn,
                              boolean tickListener,
                              boolean renderListener)
    {
        this(theEntityIn, typeIn.text, tickListener, renderListener);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn,
                              String nameIn,
                              boolean tickListener,
                              boolean renderListener)
    {
        this(theEntityIn, nameIn, UUID.randomUUID().toString(), tickListener, renderListener);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn,
                              ComponentType typeIn,
                              String idIn,
                              boolean tickListener,
                              boolean renderListener)
    {
        this(theEntityIn, typeIn.text, idIn, tickListener, renderListener);
    }
    
    //------------------------------------------------------------------------------------------
    
    protected EntityComponent(ComponentBasedObject theEntityIn,
                              String nameIn,
                              String idIn,
                              boolean tickListener,
                              boolean renderListener)
    {
        theObject = theEntityIn;
        componentName = nameIn;
        componentID = idIn;
        respondsToGameTick = tickListener;
        respondsToRenderTick = renderListener;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EntityComponentDTO toDto() {
        return null;
    }
    
    //=====================================================================================
    
    public void onRenderTick(float deltaTime) {}
    public void onGameTick(float deltaTime) {}
    
    //=====================================================================================
    
    public boolean hasFunction(String name) { return functions.containsKey(name); }
    public Map<String, Method> getOfferedFunctions() { return functions; }
    
    public <R> R callFunction(String funcName, Object... args) {
        try { return (R) functions.get(funcName).invoke(this, args); }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Call to pass an event with arguments to the parent entity.
     */
    public void onEvent(Object... args) {
        theObject.onComponentEvent(this, componentID, args);
    }    
    //=========
    // Getters
    //=========
    
    public ComponentBasedObject getBaseObject() { return theObject; }
    public String getComponentName() { return componentName; }
    public String getComponentID() { return componentID; }
    public boolean respondsToGameTick() { return respondsToGameTick; }
    public boolean respondsToRenderTick() { return respondsToRenderTick; }
    
}

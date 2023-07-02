package envision.game.component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class EntityComponent {
	
	//========
	// Fields
	//========
	
	protected final ComponentBasedObject theObject;
	protected final String componentName;
	protected final String componentID;
	protected final boolean respondsToGameTick;
	protected final boolean respondsToRenderTick;
	protected final Map<String, Method> functions = new HashMap<>();
	
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
	
	//=========
	// Getters
	//=========
	
	public ComponentBasedObject getBaseObject() { return theObject; }
	public String getComponentName() { return componentName; }
	public String getComponentID() { return componentID; }
	public boolean respondsToGameTick() { return respondsToGameTick; }
	public boolean respondsToRenderTick() { return respondsToRenderTick; }
	
}

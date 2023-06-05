package envision.game.component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import envision.game.entities.ComponentBasedObject;

public abstract class EntityComponent {
	
	protected final ComponentBasedObject theObject;
	protected final String componentName;
	protected final Map<String, Method> functions = new HashMap<>();
	protected boolean respondsToGameTick = false;
	
	//==============
	// Constructors
	//==============
	
	protected EntityComponent(ComponentBasedObject  theEntityIn, ComponentType typeIn) {
		this(theEntityIn, typeIn.text);
	}
	
	protected EntityComponent(ComponentBasedObject  theEntityIn, String nameIn) {
		theObject = theEntityIn;
		componentName = nameIn;
	}
	
	//=====================================================================================
	
	public void onRenderTick() {}
	public void onGameTick(float deltaTime) {}
	
	public ComponentBasedObject  getEntity() { return theObject; }
	public String getComponentName() { return componentName; }
	public boolean respondsToGameTick() { return respondsToGameTick; }
	
	public void setRespondsToGameTick(boolean val) { respondsToGameTick = val; }
	
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
	
}

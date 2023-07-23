package envision.game.component;

import envision.game.GameObject;

public abstract class ComponentBasedObject extends GameObject {
	
	/** The internal component system for this object. */
	public EntityComponentSystem ecs;
	
	//==============
	// Constructors
	//==============
	
	protected ComponentBasedObject() { this(null); }
	protected ComponentBasedObject(String nameIn) {
		super(nameIn);
		
		ecs = new EntityComponentSystem();
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onGameTick(float deltaTime) {
		super.onGameTick(deltaTime);
		ecs.onGameTick(deltaTime);
	}
	
	@Override
	public void onRenderTick(float deltaTime) {
		super.onRenderTick(deltaTime);
		ecs.onRenderTick(deltaTime);
	}
	
	//=========
	// Methods
	//=========
	
	/**
	 * Used by components to notify the object when an event related to some
	 * specific component has occurred.
	 */
	protected void onComponentEvent_i(EntityComponent theComponent, String id, Object... args) {
		onComponentEvent(theComponent, id, args);
	}
	
    /**
     * Used by components to notify the object when an event related to some
     * specific component has occurred.
     */
    protected void onComponentEvent(EntityComponent theComponent, String id, Object... args) {
        // do nothing by default;
    }
	
	//==================
	// Wrapped From ECS
	//==================
	
	/** @see EntityComponentSystem#addComponent(EntityComponent) */
	public boolean addComponent(EntityComponent component) {
		return ecs.addComponent(component);
	}
	
	/** @see EntityComponentSystem#removeComponent(EntityComponent) */
	public boolean removeComponent(EntityComponent component) {
		return ecs.removeComponent(component);
	}
	
	/** @see EntityComponentSystem#removeComponent(ComponentType) */
	public boolean removeComponent(ComponentType componentType) {
		return ecs.removeComponent(componentType.text);
	}
	
	/** @see EntityComponentSystem#removeComponent(String) */
	public boolean removeComponent(String componentName) {
		return ecs.removeComponent(componentName);
	}
	
	/** @see EntityComponentSystem#getComponent(ComponentType) */
	public <E extends EntityComponent> E getComponent(ComponentType componentType) {
		return ecs.getComponent(componentType);
	}
	
	/** @see EntityComponentSystem#getComponent(String) */
	public <E extends EntityComponent> E getComponent(String componentName) {
		return ecs.getComponent(componentName);
	}
	
	/** @see EntityComponentSystem#getComponentIndex(ComponentType) */
	public int getComponentIndex(ComponentType componentType) {
		return ecs.getComponentIndex(componentType);
	}
	
	/** @see EntityComponentSystem#getComponentIndex(String) */
	public int getComponentIndex(String componentName) {
		return ecs.getComponentIndex(componentName);
	}

	/** @see EntityComponentSystem#hasComponent(ComponentType) */
	public boolean hasComponent(ComponentType componentType) {
		return ecs.hasComponent(componentType);
	}
	
	/** @see EntityComponentSystem#hasComponent(String) */
	public boolean hasComponent(String componentName) {
		return ecs.hasComponent(componentName);
	}
	
	//=========
	// Getters
	//=========
	
	/** Returns the internal component system for this object. */
	public EntityComponentSystem getComponentSystem() { return ecs; }
	
}

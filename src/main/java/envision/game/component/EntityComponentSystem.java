package envision.game.component;

import java.util.Objects;

import eutil.datatypes.util.EList;

public class EntityComponentSystem {
	
	//========
	// Fields
	//========
	
	private EList<EntityComponent> components = EList.newList();
	
	//=========
	// Methods
	//=========
	
	public boolean addComponent(EntityComponent component) {
		Objects.requireNonNull(component);
		return components.add(component);
	}
	
	public boolean removeComponent(EntityComponent component) {
		Objects.requireNonNull(component);
		return components.remove(component);
	}
	
	public boolean removeComponent(ComponentType componentType) {
		return removeComponent(componentType.text);
	}
	
	public boolean removeComponent(String componentName) {
		int index = getComponentIndex(componentName);
		if (index == -1) return false;
		components.remove(index);
		return true;
	}
	
	/**
	 * Returns a component on this component system with the given name.
	 * 
	 * @param ComponentName The name of the component to find
	 * @return The found component or null if it doesn't exist 
	 */
	public <E extends EntityComponent> E getComponent(ComponentType componentType) {
		return getComponent(componentType.text);
	}
	
	/**
	 * Returns a component on this component system with the given name.
	 * 
	 * @param ComponentName The name of the component to find
	 * @return The found component or null if it doesn't exist 
	 */
	public <E extends EntityComponent> E getComponent(String componentName) {
		final int size = components.size();
		for (int i = 0; i < size; i++) {
			var c = components.get(i);
			if (c.getComponentName().equals(componentName)) return (E) c;
		}
		return null;
	}
	
	/**
	 * Returns the index of a component with the given name or -1 if this
	 * component system does not have a component with that name.
	 * 
	 * @param componentName The name of the component to find
	 * @return The index position of the component or -1 if not found
	 */
	public int getComponentIndex(ComponentType componentType) {
		return getComponentIndex(componentType.text);
	}
	
	/**
	 * Returns the index of a component with the given name or -1 if this
	 * component system does not have a component with that name.
	 * 
	 * @param componentName The name of the component to find
	 * @return The index position of the component or -1 if not found
	 */
	public int getComponentIndex(String componentName) {
		final int size = components.size();
		for (int i = 0; i < size; i++) {
			var c = components.get(i);
			if (c.getComponentName().equals(componentName)) return i;
		}
		return -1;
	}
	
	/**
	 * Returns true if this component system contains a component under the
	 * given name.
	 */
	public boolean hasComponent(ComponentType componentType) {
		return getComponent(componentType.text) != null;
	}
	
	/**
	 * Returns true if this component system contains a component under the
	 * given name.
	 */
	public boolean hasComponent(String componentName) {
		return getComponent(componentName) != null;
	}
	
}

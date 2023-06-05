package envision.game.component.types;

import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;
import envision.game.entities.ComponentBasedObject;

public abstract class TimedEventComponent extends EntityComponent {
	
	protected TimedEventComponent(ComponentBasedObject theEntityIn) {
		super(theEntityIn, ComponentType.TIMED_EVENT);
		respondsToGameTick = true;
	}
	
	@Override
	public void onGameTick(float deltaTime) {
		
	}
	
	
	
}

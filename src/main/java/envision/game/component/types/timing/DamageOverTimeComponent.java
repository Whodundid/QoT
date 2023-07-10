package envision.game.component.types.timing;

import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;
import envision.game.entities.Entity;

public class DamageOverTimeComponent extends EntityComponent {
	
	protected DamageOverTimeComponent(Entity theEntityIn) {
		super(theEntityIn, ComponentType.DAMAGE_OVER_TIME);
	}
	
	@Override
	public void onGameTick(float deltaTime) {
		
	}
	
}

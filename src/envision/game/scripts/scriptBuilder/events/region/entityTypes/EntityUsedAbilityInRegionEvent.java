package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.abilities.Ability;
import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
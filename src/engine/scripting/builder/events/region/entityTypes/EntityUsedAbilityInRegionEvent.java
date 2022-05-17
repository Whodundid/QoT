package engine.scripting.builder.events.region.entityTypes;

import engine.scripting.builder.events.region.EntityRegionEvent;
import game.abilities.Ability;
import game.entities.Entity;
import world.GameWorld;
import world.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
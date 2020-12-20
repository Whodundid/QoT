package gameSystems.scriptingSystem.gameScripting.events.region.entityTypes;

import assets.abilities.Ability;
import assets.entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.events.region.EntityRegionEvent;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
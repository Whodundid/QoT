package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.objects.abilities.Ability;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
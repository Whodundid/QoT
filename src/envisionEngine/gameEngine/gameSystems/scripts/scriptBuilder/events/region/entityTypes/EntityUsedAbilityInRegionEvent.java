package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envision.gameEngine.gameObjects.abilities.Ability;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
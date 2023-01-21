package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envisionEngine.gameEngine.gameObjects.abilities.Ability;
import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
	
	Ability ability;
	
	public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
		super(theWorld, theRegion, theEntity);
	}
	
	public Ability getAbility() { return ability; }
	
}
package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class EntityWasHurtInRegionEvent extends EntityRegionEvent {
	
	private int damage = 0;
	
	public EntityWasHurtInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int damageIn) {
		super(theWorld, theRegion, theEntity);
		damage = damageIn;
	}
	
	public int getDamage() { return damage; }
	
}
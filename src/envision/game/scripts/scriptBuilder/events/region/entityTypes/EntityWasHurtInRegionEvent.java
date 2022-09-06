package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class EntityWasHurtInRegionEvent extends EntityRegionEvent {
	
	private int damage = 0;
	
	public EntityWasHurtInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int damageIn) {
		super(theWorld, theRegion, theEntity);
		damage = damageIn;
	}
	
	public int getDamage() { return damage; }
	
}
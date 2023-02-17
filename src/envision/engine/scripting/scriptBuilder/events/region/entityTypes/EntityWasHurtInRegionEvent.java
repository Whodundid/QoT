package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityWasHurtInRegionEvent extends EntityRegionEvent {
	
	private int damage = 0;
	
	public EntityWasHurtInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int damageIn) {
		super(theWorld, theRegion, theEntity);
		damage = damageIn;
	}
	
	public int getDamage() { return damage; }
	
}
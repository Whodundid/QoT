package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.loader.built.game.Ability;
import envision.engine.loader.built.game.Entity;
import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityUsedAbilityInRegionEvent extends EntityRegionEvent {
    
    Ability ability;
    
    public EntityUsedAbilityInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Ability theAbility) {
        super(theWorld, theRegion, theEntity);
    }
    
    public Ability getAbility() { return ability; }
    
}
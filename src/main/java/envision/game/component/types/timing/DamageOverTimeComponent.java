package envision.game.component.types.timing;

import envision.engine.loader.built.game.Entity;
import envision.engine.loader.built.game.EntityComponent;
import envision.game.component.ComponentType;

public class DamageOverTimeComponent extends EntityComponent {
    
    protected DamageOverTimeComponent(Entity theEntityIn) {
        super(theEntityIn, ComponentType.DAMAGE_OVER_TIME);
    }
    
    @Override
    public void onGameTick(float deltaTime) {
        
    }
    
}

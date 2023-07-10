package envision.game.component.types.death;

import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;
import envision.game.entities.Entity;

public class OnDeathComponent extends EntityComponent {
    
    /**
     * Builds a new 'On Death' component for the given entity.
     * <p>
     * When the given entity dies, it will trigger an event in this
     * component.
     * 
     * @param theEntityWhoWillDie The entity that will die
     */
    protected OnDeathComponent(ComponentBasedObject theEntityWhoWillDie) {
        super(theEntityWhoWillDie, ComponentType.ON_DEATH);
    }
    
    public void onDeath(Entity killingEntity) {
        onEvent(killingEntity);
    }
    
}

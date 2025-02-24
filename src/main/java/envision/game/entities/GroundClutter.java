package envision.game.entities;

import envision.engine.loader.built.game.Entity;

/** An entity that will never draw transparent even if the camera's entity is behind it. */
public abstract class GroundClutter extends Entity {
    
    protected GroundClutter() { this(null); }
    protected GroundClutter(String nameIn) {
        super(nameIn);
        
        addComponent(new EntityRenderer(this, false));
        this.canBeMoved = false;
        this.canBeCarried = false;
        this.canMoveEntities = false;
        this.canRegenHealth = false;
        this.canRegenMana = false;
    }
    
}

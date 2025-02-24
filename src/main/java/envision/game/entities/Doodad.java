package envision.game.entities;

import envision.engine.loader.built.game.Entity;

/** An entity that will draw transparent if the camera's entity is behind it. */
public abstract class Doodad extends Entity {
    
    protected Doodad() { this(null); }
    protected Doodad(String nameIn) {
        super(nameIn);
        
        addComponent(new EntityRenderer(this, true));
        this.canBeMoved = false;
        this.canBeCarried = false;
        this.canMoveEntities = false;
        this.canRegenHealth = false;
        this.canRegenMana = false;
    }
    
}

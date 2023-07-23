package envision.game.entities;

/** An entity that will draw transparent if the camera's entity is behind it. */
public abstract class Doodad extends Entity {
    
    protected Doodad() { this(null); }
    protected Doodad(String nameIn) {
        super(nameIn);
        
        addComponent(new EntityRenderer(this, true));
    }
    
}

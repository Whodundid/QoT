package envision.game.entities;

/** An entity that has a standard entity renderer component and nothing else. */
public abstract class BasicRenderedEntity extends Entity {
	
	protected BasicRenderedEntity() { this(null); }
	protected BasicRenderedEntity(String nameIn) {
		super(nameIn);
		
		addComponent(new EntityRenderer(this, false));
	}
	
}

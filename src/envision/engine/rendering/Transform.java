package envision.engine.rendering;

import org.joml.Vector2f;

public class Transform {
	
	public Vector2f position;
	public Vector2f scale;
	
	public Transform() {
		this(new Vector2f(), new Vector2f(1f, 1f));
	}
	
	public Transform(Vector2f positionIn) {
		this(positionIn, new Vector2f(1f, 1f));
	}
	
	public Transform(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
	}
	
}

package envision.engine.rendering;

import org.joml.Vector3f;

public class Transform {
	
	public Vector3f position;
	public Vector3f scale;
	public Vector3f rotation;
	
	public Transform() {
		this(new Vector3f(), new Vector3f(1f, 1f, 1f), new Vector3f());
	}
	
	public Transform(Vector3f positionIn) {
		this(positionIn, new Vector3f(1f, 1f, 1f), new Vector3f());
	}
	
	public Transform(Vector3f position, Vector3f scale, Vector3f rotation) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
}

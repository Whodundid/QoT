package envision.engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import eutil.math.vectors.Vec2f;

public class Camera {
	
	public Matrix4f projection, view;
	public Vector3f position;
	
	public Camera() { this(0f, 0f, 20f); }
	public Camera(float x, float y) { this(new Vector3f(x, y, 20f)); }
	public Camera(float x, float y, float z) { this(new Vector3f(x, y, z)); }
	public Camera(Vec2f positionIn) { this(new Vector3f(positionIn.x, positionIn.y, 20f)); }
	public Camera(Vector3f positionIn) {
		position = positionIn;
		projection = new Matrix4f();
		view = new Matrix4f();
	}
	
	public void updateProjection(double newWidth, double newHeight) {
		projection.identity();
		projection.ortho(0, (float) newWidth, (float) newHeight, 0, 1f, 100f);
	}
	
	public Matrix4f getView() {
		Vector3f eye = new Vector3f(position.x, position.y, position.z);
		Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
		Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
		
		cameraFront.add(position.x, position.y, 0.0f);
		
		view.identity();
		view.lookAt(eye, cameraFront, cameraUp);
		
		return view;
	}
	
	public Matrix4f getProjection() {
		return projection;
	}
	
}

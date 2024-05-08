package envision.engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ObjectTransform extends RenderingManager {
	
    //========
    // Fields
    //========
    
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private Matrix4f transform;
	
	//==============
    // Constructors
    //==============
	
	public ObjectTransform() {
		this(new Vector3f(), new Vector3f(), new Vector3f(1f, 1f, 1f));
	}
	
	public ObjectTransform(Vector3f positionIn) {
		this(positionIn, new Vector3f(), new Vector3f(1f, 1f, 1f));
	}
	
	public ObjectTransform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	//=========
    // Methods
    //=========
	
	public void updateMatrix() {
	    transform.identity();
	    transform.translate(position.x, position.y, position.z);
	    transform.rotateXYZ(rotation.x, rotation.y, rotation.z);
	    transform.scale(scale.x, scale.y, scale.z);
	}
	
	//=========
    // Getters
    //=========
	
    public Vector3f getModelPosition() { return position; }
    public Vector3f getModelRotation() { return rotation; }
    public Vector3f getModelScale() { return scale; }
    public Matrix4f getTransformMatrix() { return transform; }
	
    //=========
    // Setters
    //=========
    
    public void moveModel(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
        updateMatrix();
    }
    
    public void setModelPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
        updateMatrix();
    }
    
    public void setModelRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
        updateMatrix();
    }
    
    public void setModelScale(float x, float y, float z) {
        scale.x = x;
        scale.y = y;
        scale.z = z;
        updateMatrix();
    }
    
    public void setModelPosition(Vector3f posIn) { position = posIn; updateMatrix(); }
    public void setModelRotation(Vector3f rotIn) { rotation = rotIn; updateMatrix(); }
    public void setModelScale(Vector3f scaleIn) { scale = scaleIn; updateMatrix(); }
    
}

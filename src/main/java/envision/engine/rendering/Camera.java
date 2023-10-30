package envision.engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import eutil.math.vectors.Vec2f;

public class Camera {
    
    public Matrix4f projection, view;
    public Vector3f position, rotation;
    public boolean perspective;
    
    private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    private static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
    
    public Camera() { this(0f, 0f, 20f); }
    public Camera(float x, float y) { this(new Vector3f(x, y, 20f)); }
    public Camera(float x, float y, float z) { this(new Vector3f(x, y, z)); }
    public Camera(Vec2f positionIn) { this(new Vector3f(positionIn.x, positionIn.y, 20f)); }
    public Camera(Vector3f positionIn) {
        position = positionIn;
        rotation = new Vector3f();
        projection = new Matrix4f();
        view = new Matrix4f();
    }
    
    public void setupForOrtho(double newWidth, double newHeight) {
        projection.identity();
        projection.ortho(0, (float) newWidth, (float) newHeight, 0, 1f, 100f);
        perspective = false;
    }
    
    public void setupForPerspective(double newWidth, double newHeight) {
        projection.identity();
        float aspect = (float) (newWidth / newHeight);
        projection.perspective(70f, aspect, 0.1f, 10000.0f);
        perspective = true;
    }
    
    public Matrix4f getView() {
        if (perspective) {
            Vector3f eye = new Vector3f(position.x, position.y, position.z);
            Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
            Vector3f cameraUp = new Vector3f(0f, -1f, 0f);
            
            cameraFront.add(position.x, position.y, 0);
            
            view.identity();
            view.rotateX(rotation.x);
            view.rotateY(rotation.y);
            view.rotateZ(rotation.z);
            view.lookAt(eye, cameraFront, cameraUp);
            
            return view;
        }
        
        Vector3f eye = new Vector3f(position.x, position.y, position.z);
        Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
        Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
        
        cameraFront.add(position.x, position.y, 0.0f);
        
        view.identity();
        view.lookAt(eye, cameraFront, cameraUp);
        
        return view;
    }
    
    public Matrix4f getProjection() { return projection; }
    
    public Vector3f getPosition() { return position; }
    
}

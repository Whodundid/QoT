package envision.game.entities.physics;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
 * 
 * @author Hunter
 */
public class RigidBody3D {
    
    //========
    // Fields
    //========
    
    private ObjectTransform rawTransform;
    
    private Vector3f forceAccumulator = new Vector3f();
    private Vector3f linearVelocity = new Vector3f();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;
    private float friction = 0.1f;
    private float gravityScale = 1.0f;
    private boolean fixedRotation;
    
    public Vector3f position = new Vector3f();
    public float rotation = 0.0f;
    /** kg */
    public float mass = 0.0f;
    public float inverseMass = 0.0f;
    
    //=========
    // Methods
    //=========
    
    public void update(float dt) {
        if (mass == 0.0f) return;
        
        // Calculate linear velocity
        Vector3f acceleration = new Vector3f(forceAccumulator).mul(inverseMass);
        linearVelocity.add(acceleration).mul(dt);
        
        // Update the linear position
        position.add(new Vector3f(linearVelocity).mul(dt));
    }
    
    public void synchCollisionTransforms() {
        if (rawTransform != null) {
            rawTransform.position.set(position);
        }
    }
    
    public void clearAccumulators() {
        forceAccumulator.zero();
    }
    
    public void addForce(Vector2f force) {
        addForce(new Vector3f(force.x, force.y, 0.0f));
    }
    
    public void addForce(Vector3f force) {
        forceAccumulator.add(force);
    }
    
    //=========
    // Getters
    //=========
    
    public Vector3f getPosition() { return position; }
    public float getRotation() { return rotation; }
    public float getMass() { return mass; }
    public float getLinearDamping() { return linearDamping; }
    public float getAngularDamping() { return angularDamping; }
    
    //=========
    // Setters
    //=========
    
    public void setMass(float massIn) {
        mass = massIn;
        if (mass != 0.0f) {
            inverseMass = 1.0f / mass;
        }
    }
    
    public void setTranform(Vector3f positionIn, float rotationIn) {
        position.set(positionIn);
        rotation = rotationIn;
    }
    
    public void setTransform(ObjectTransform transformIn) {
        rawTransform = transformIn;
        position.set(rawTransform.position);
    }
    
}

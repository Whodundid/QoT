package envision.game.entities.physics;

import org.joml.Vector3f;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;

// referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
public class EntityPhysicsHandler {
    
    //========
    // Fields
    //========
    
    private Entity theEntity;
    
    public float posX, posY, posZ;
    public float xMotion, yMotion, zMotion;
    public float prevPosX, prevPosY, prevPosZ;
    public float prevMotionX, prevMotionY, prevMotionZ;
    
    private long timeSinceLastMove;
    private long timeSinceLastUpdate;
    
    private ForceRegistry forceRegistry;
    private EList<RigidBody3D> rigidBodies = EList.newList();
    private Gravity3D gravity;
    private float fixedUpdate;
    
    //==============
    // Constructors
    //==============
    
    public EntityPhysicsHandler(Entity entity) {
        theEntity = entity;
        
        forceRegistry = new ForceRegistry();
        fixedUpdate = 1.0f / 60.0f;
        gravity = new Gravity3D(new Vector3f(0.0f, 0.0f, -9.81f));
    }
    
    //=========
    // Methods
    //=========
    
    public void update(float dt) {
        fixedUpdate();
    }
    
    public void fixedUpdate() {
        forceRegistry.updateForces(fixedUpdate);
        
        // Update the velocities of all rigid bodies
        final int len = rigidBodies.size();
        for (int i = 0; i < len; i++) {
            rigidBodies.get(i).update(fixedUpdate);
        }
    }
    
    /**
     * Moves this entity by the given distance in X/Y world coordinates over
     * the given duration. If this entity hits a wall while moving, the entity
     * will stop moving in the direction that it hit the wall, but continue in
     * the other (if possible) for the rest of the duration.
     * 
     * @param x             world coordinates in X axis
     * @param y             world coordinates in Y axis
     * @param timeInSeconds The amount of time that the move will take place over
     */
    public void moveEntityByAmountOverTime(double x, double y, long timeInSeconds) {
        
    }
    
    /**
     * Applies the given vector X, and Y (in world coorinates per second) to
     * this entity for this game tick specifically. This impulse is removed on
     * the next game tick.
     */
    public void applyImpulse(double x, double y) {
        
        // TEMP BS
        theEntity.getCollisionHelper().tryMovePixel(x, y);
    }
    
    /**
     * Applies the given vector X, and Y (in world coordinates per second) to
     * this entity. This force remains until counter-acted by an opposing force.
     * 
     * @param x Vector component X
     * @param y Vector component Y
     */
    public void applyForce(double x, double y) {
        
    }
    
    //public void removeXForce
    
    public void applyQuadraticForce() {
        
    }
    
    public void addRigidBody(RigidBody3D body) {
        rigidBodies.add(body);
        forceRegistry.add(body, gravity);
    }
    
    public void removeRigidBody(RigidBody3D body) {
        rigidBodies.remove(body);
        forceRegistry.remove(body, gravity);
    }
    
    //=========
    // Getters
    //=========
    
    public Entity getEntity() { return theEntity; }
    public float getFixedUpdateRate() { return fixedUpdate; }
    
    //=========
    // Setters
    //=========
    
    public void setFixedUpdateRate(float rateIn) { fixedUpdate = rateIn; }
    
}

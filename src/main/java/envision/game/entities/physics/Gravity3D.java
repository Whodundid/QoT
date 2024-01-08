package envision.game.entities.physics;

import org.joml.Vector3f;

public class Gravity3D implements ForceGenerator {

    //========
    // Fields
    //========
    
    private Vector3f gravity;
    
    //==============
    // Constructors
    //==============
    
    public Gravity3D(Vector3f force) {
        gravity = new Vector3f(force);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void updateForce(RigidBody3D body, float dt) {
        body.addForce(new Vector3f(gravity).mul(body.getMass()));
    }
    
}

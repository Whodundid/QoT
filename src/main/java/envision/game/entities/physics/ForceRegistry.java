package envision.game.entities.physics;

import eutil.datatypes.util.EList;

/**
 * Referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
 * 
 * @author Hunter
 */
public class ForceRegistry {
    
    //========
    // Fields
    //========
    
    private EList<ForceRegistration> registry;
    
    //==============
    // Constructors
    //==============
    
    public ForceRegistry() {
        registry = EList.newList();
    }
    
    //=========
    // Methods
    //=========
    
    public void add(RigidBody3D rb, ForceGenerator fg) {
        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.add(fr);
    }

    public void remove(RigidBody3D rb, ForceGenerator fg) {
        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.remove(fr);
    }

    public void clear() {
        registry.clear();
    }

    public void updateForces(float dt) {
        for (ForceRegistration fr : registry) {
            fr.fg.updateForce(fr.rb, dt);
        }
    }

    public void zeroForces() {
        for (ForceRegistration fr : registry) {
            // TODO: IMPLEMENT ME
            //fr.rb.zeroForces();
        }
    }
    
}

package envision.game.entities.physics;

/**
 * Referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
 * 
 * @author Hunter
 */
public class ForceRegistration {
    
    //========
    // Fields
    //========
    
    public ForceGenerator fg;
    public RigidBody3D rb;
    
    //==============
    // Constructors
    //==============
    
    public ForceRegistration(ForceGenerator fgIn, RigidBody3D rbIn) {
        fg = fgIn;
        rb = rbIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != ForceRegistration.class) return false;
        
        ForceRegistration fr = (ForceRegistration) other;
        return fr.rb == rb && fr.fg == fg;
    }
    
}

package envision.game.entities.physics;

import org.joml.Vector3f;

/**
 * Referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
 * 
 * @author Hunter
 */
public class ObjectTransform {
    
    //========
    // Fields
    //========
    
    public Vector3f position;
    public Vector3f scale;
    
    //==============
    // Constructors
    //==============
    
    public ObjectTransform() {
        init(new Vector3f(), new Vector3f());
    }
    
    public ObjectTransform(Vector3f positionIn) {
        init(positionIn, new Vector3f());
    }
    
    public ObjectTransform(Vector3f positionIn, Vector3f scaleIn) {
        init(positionIn, scaleIn);
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    public void init(Vector3f positionIn, Vector3f scaleIn) {
        position = positionIn;
        scale = scaleIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof ObjectTransform)) return false;
        
        ObjectTransform t = (ObjectTransform) o;
        return t.position.equals(position) && t.scale.equals(scale);
    }
    
    //=========
    // Methods
    //=========
    
    public ObjectTransform copy() {
        return new ObjectTransform(new Vector3f(position), new Vector3f(scale));
    }
    
    public void copy(ObjectTransform to) {
        to.position.set(position);
        to.scale.set(scale);
    }
    
}

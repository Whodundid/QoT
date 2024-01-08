package envision.game.entities.physics;

/**
 * Referenced 'GamesWithGabe' - https://github.com/codingminecraft/MarioYoutube
 * 
 * @author Hunter
 */
public interface ForceGenerator {
    
    void updateForce(RigidBody3D body, float dt);
    
}

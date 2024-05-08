package qot.particles;

import envision.game.animations.AnimationHandler;
import envision.game.particles.Particle;
import qot.assets.textures.effects.EffectsTextures;

public class FireEffect extends Particle {
    
    //==============
    // Constructors
    //==============
    
    public FireEffect(double x, double y) {
        this(x, y, 0, 0, 1500);
    }
    
    public FireEffect(double x, double y, double w, double h, long timeToLive) {
        super("Fire", timeToLive);
        init(0, 0, w, h);
        setPixelPos((int) x, (int) y);
        
        animationHandler = new AnimationHandler(this);
        
        var Fire = animationHandler.createAnimationSet("Fire");
        Fire.fromSpriteSheet(EffectsTextures.fire_effect_spritesheet);
        long interval = timeToLive / Fire.getNumberOfFrames();
        Fire.setUpdateInterval(interval / 2);
        
        animationHandler.setCurrentAnimation("Fire");
        animationHandler.playIfNotAlreadyPlaying("Fire");
    }
    
}

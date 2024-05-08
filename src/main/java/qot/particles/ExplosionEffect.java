package qot.particles;

import envision.game.animations.AnimationHandler;
import envision.game.particles.Particle;
import qot.assets.textures.effects.EffectsTextures;

public class ExplosionEffect extends Particle {
    
    //==============
    // Constructors
    //==============
    
    public ExplosionEffect(double x, double y) {
        this(x, y, 0, 0, 1500);
    }
    
    public ExplosionEffect(double x, double y, double w, double h, long timeToLive) {
        super("Explosion", timeToLive);
        init(0, 0, w, h);
        setPixelPos((int) x, (int) y);
        
        animationHandler = new AnimationHandler(this);
        
        var explode = animationHandler.createAnimationSet("Explode");
        explode.fromSpriteSheet(EffectsTextures.explosion_effect_spritesheet);
        long interval = timeToLive / explode.getNumberOfFrames();
        explode.setUpdateInterval(interval);

        animationHandler.setCurrentAnimation("Explode");
        animationHandler.playIfNotAlreadyPlaying("Explode");
    }
    
}

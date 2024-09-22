package envision.game.particles;

import envision.Envision;
import envision.game.entities.Doodad;

public abstract class Particle extends Doodad {
    
    //========
    // Fields
    //========
    
    protected long timeAlive;
    protected long timeToLive;
    
    //==============
    // Constructors
    //==============
    
    protected Particle() { this(null, 1000); }
    protected Particle(String nameIn, long timeToLiveIn) {
        super(nameIn);
        
        timeToLive = timeToLiveIn;
        setInvincible(true);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onLivingUpdate(float dt) {
        if (animationHandler != null) {
            animationHandler.onRenderTick((long) dt);
        }
        timeAlive += dt;
        if (timeAlive >= timeToLive) {
            kill();
            Envision.theWorld.removeObjectFromWorld(this);
        }
    }
    
    @Override
    public int getInternalSaveID() {
        return -1;
    }
    
}

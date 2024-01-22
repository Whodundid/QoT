package envision.game.entities.combat;

import org.joml.Vector3f;

import envision.game.entities.Entity;
import eutil.misc.Rotation;

public class DodgeRoll {
    
    //========
    // Fields
    //========
    
    private Entity ent;
    private boolean isRolling = false;
    private Vector3f rollingDir;
    private long timeSpentRolling = 0;
    private long rollDuration = 1200;
    private double rollSpeed = 50;
    private boolean isRollingLeft = false;
    private int previousState = 0;
    private boolean wasInvincibleBefore = false;
    
    //=========
    // Methods
    //=========
    
    public void performDodgeRoll(Entity entIn, Vector3f dir, long duration, double rollSpeedIn) {
        if (isRolling || entIn == null) return;
        
        ent = entIn;
        isRolling = true;
        rollingDir = dir;
        rollSpeed = rollSpeedIn;
        rollDuration = duration;
        ent.canMoveAcrossLowMovementBlockingWalls = true;
        timeSpentRolling = 0;
        ent.preventMovementInputs = true;
        
        previousState = 1;
        isRollingLeft = (dir.x < 0);
        wasInvincibleBefore = ent.invincible;
        ent.invincible = true;
    }
    
    public void update(long dt) {
        if (!isRolling) return;
        
        timeSpentRolling += dt;
        
        if (timeSpentRolling >= rollDuration) {
            isRolling = false;
            ent.canMoveAcrossLowMovementBlockingWalls = false;
            ent.forcedRotation = null;
            ent.preventMovementInputs = false;
            if (!wasInvincibleBefore) ent.invincible = false;
            return;
        }
        
        float amount = (float) timeSpentRolling / (float) rollDuration;
        int state = (int) (amount * 5);
        if (state > previousState) previousState = state;
        
        if (isRollingLeft) {
            switch (state % 5) {
            case 1: ent.forcedRotation = Rotation.LEFT; break;
            case 2: ent.forcedRotation = Rotation.DOWN; break;
            case 3: ent.forcedRotation = Rotation.RIGHT; break;
            case 4: ent.forcedRotation = null; break;
            default: break;
            }            
        }
        else {
            switch (state % 5) {
            case 1: ent.forcedRotation = Rotation.RIGHT; break;
            case 2: ent.forcedRotation = Rotation.DOWN; break;
            case 3: ent.forcedRotation = Rotation.LEFT; break;
            case 4: ent.forcedRotation = null; break;
            default: break;
            }
        }
        
        Vector3f move = rollingDir.mul((float) rollSpeed * dt, new Vector3f());
        ent.physicsHandler.applyImpulse(move.x, move.y);
    }
    
    //=========
    // Getters
    //=========
    
    public boolean isRolling() { return isRolling; }
    
}

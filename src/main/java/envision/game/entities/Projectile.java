package envision.game.entities;

import org.joml.Vector3f;

import qot.particles.FloatingTextEntity;

public abstract class Projectile extends BasicRenderedEntity {
    
    //========
    // Fields
    //========
    
    /**
     * The maximum amount of time that this projectile will travel for before
     * being deleted or until it collides with a wall.
     */
    protected float maxLifeSpan;
    
    /** The amount of time this projectile has been alive for. */
    protected float currentLifeSpan;
    /** True if this projectile has actually been spawned into the world. */
    protected boolean spawned = false;
    
    /** The entity who fired this projectile. */
    protected Entity firingEntity;
    /** The normalized direction this projectile is flying in. */
    protected Vector3f direction;
    
    /** The maximum number of enemies this projectile can damage before dying. */
    protected int maxDamageCount;
    /** The current number of entities this projectile has damaged. */
    protected int damageCount;
    
    //==============
    // Constructors
    //==============
    
    protected Projectile() { this(null); }
    protected Projectile(String nameIn) {
        super(nameIn);
        
        canBeMoved = false;
        canMoveEntities = false;
        canBeCarried = false;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onLivingUpdate(float dt) {
        if (!spawned) {
            spawned = true;
        }
        
        currentLifeSpan += dt;
        
        if (currentLifeSpan >= maxLifeSpan) {
            killProjectile();
            return;
        }
        
        final double speed = this.getSpeed();
        // move towards fired direction
        move(direction.x * speed, direction.y * speed);
        
        var cb = getCollisionDims();
        
        //final double w = (width * width) / world.getTileWidth();
        final var entitiesInRange = world.getAllEntitiesWithinDistance(this, 50);
        
        for (Entity e : entitiesInRange) {
            // ignore the entity who fired this projectile
            if (e == firingEntity) continue;
            // don't kill yourself :)
            if (e == this) continue;
            // other checks
            if (e instanceof FloatingTextEntity) continue;
            //if (e instanceof Doodad) continue;
            //if (e.isInvincible()) continue;
            if (e.getClass() == this.getClass()) continue;
            if (e.isDead()) continue;
            
            var pcb = e.getCollisionDims();
            if (cb.partiallyContains(pcb)) {
                //System.out.println(e);
                e.attackedBy(firingEntity, getBaseMeleeDamage());
                
                damageCount++;
                if (damageCount >= maxDamageCount) {
                    killProjectile();                    
                }
            }
        }
        
        if (collisionHelper.isCollided()) {
            killProjectile();
            return;
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void killProjectile() {
        kill();
        world.removeEntity(this);
    }
    
    //=========
    // Getters
    //=========
    
    public float getMaxLifeSpan() { return maxLifeSpan; }
    public float getCurrentLifeSpan() { return currentLifeSpan; }
    
    //=========
    // Setters
    //=========
    
    public void setMaxLifeSpan(float timeInSeconds) { maxLifeSpan = timeInSeconds; }
    public void setMaxDamageCount(int max) { maxDamageCount = max; }
    
    public void setFiredDirection(Vector3f directionIn) {
        direction = directionIn;
    }
    
    public void setFiringEntity(Entity in) {
        firingEntity = in;
    }
    
}

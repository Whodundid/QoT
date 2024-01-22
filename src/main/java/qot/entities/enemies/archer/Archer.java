package qot.entities.enemies.archer;

import org.joml.Vector3f;

import envision.Envision;
import envision.engine.registry.types.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.Enemy;
import envision.game.entities.combat.EntityAttack;
import envision.game.world.GameWorld;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class Archer extends Enemy {
    
    private boolean hit = false;
    private long timeSinceLastHit;
    private long timeSinceLastFireball;
    private long fireballDelay = 3000;
    
    public Archer() { this("Archer", 0, 0); }
    public Archer(String nameIn, int x, int y) {
        super(nameIn);
        
        setBaseMeleeDamage(3);
        setMaxHealth(15);
        setHealth(15);
        
        init(x, y, 32, 64);
        sprite = new Sprite(EntityTextures.player);
        
        setCollisionBox(startX + 8, endY - 15, endX - 8, endY);
        setExperienceRewardedOnKill(75);
        
        setSpeed(50);
        
        // item on death
        
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, Items.random());
        itemOnDeath.setChance(5);
        
        addComponent(itemOnDeath);
    }
    
    @Override
    public void onLivingUpdate(float dt) {
        super.onLivingUpdate(dt);
    }
    
    @Override
    protected void runPassiveAI(float dt) {
        wander();
    }
    
    @Override
    protected void runAggressiveAI(float dt) {
        if (currentTarget == null) {
            runPassiveAI(dt);
            return;
        }
        
        double dist = world.getDistance(this, currentTarget);
        //this.headText = "" + new DecimalFormat("#.00").format(dist);
        //this.headText = "";
        
        // wander around if player is not near
        if (dist > 300) {
            wander();
        }
        // get closer to the player
        else if (dist <= 300 && dist > 200) {
            moveTowardsPlayer();
        }
        // shoot arrows at the player
        else if (dist <= 200 && dist >= 75) {
            shootArrow();
        }
        // else do melee stuff
        else {
            doMeleeStuff();
        }
    }
    
    @Override
    protected void wander() {
        super.wander();
    }
    
    private void moveTowardsPlayer() {
        Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
        move(dirToPlayer);
    }
    
    private void shootArrow() {
        if (Envision.thePlayer == null) return;
        
//        var left = switch (facing) {
//        case LEFT, UP -> true;
//        default -> false;
//        };
        
        if (System.currentTimeMillis() - timeSinceLastFireball >= fireballDelay) {
            timeSinceLastFireball = System.currentTimeMillis();
            
            float diffX = (float) (midX - Envision.thePlayer.midX);
            float diffY = (float) (midY - Envision.thePlayer.midY);
            Vector3f dir = new Vector3f(diffX, diffY, 0.0f);
            dir.normalize();
            dir.mul(-1.0f);
            
            var fb = new Arrow();
            fb.setBaseMeleeDamage(3);
            fb.setFiredDirection(dir);
            fb.setFiringEntity(this);
            fb.startX = midX - width * 0.5;
            fb.startY = midY - height * 0.5;
            
            world.addEntity(fb);
        }
    }
    
    private void doMeleeStuff() {
        Dimension_d testDim = getCollisionDims();
        Dimension_d pDims = Envision.thePlayer.getCollisionDims();
        
        if (testDim.partiallyContains(pDims)) {
            if (hit) {
                //System.out.println(System.currentTimeMillis() - timeSinceLastHit);
                if ((System.currentTimeMillis() - timeSinceLastHit) >= 200) {
                    hit = false;
                }
            }
            else {
                hit = true;
                timeSinceLastHit = System.currentTimeMillis();
                int amount = EntityAttack.calculateMeleeAttackDamage(this);
                Envision.thePlayer.attackedBy(this, amount);
            }
        }
        
        Direction dirToPlayer = world.getDirectionTo(this, Envision.thePlayer);
        move(dirToPlayer);
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.ARCHER.ID;
    }
    
}

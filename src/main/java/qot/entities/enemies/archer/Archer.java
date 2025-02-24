package qot.entities.enemies.archer;

import org.joml.Vector3f;

import envision.Envision;
import envision.engine.loader.built.game.Sprite;
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
        wander(dt);
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
            wander(dt);
        }
        // get closer to the player
        else if (dist <= 300 && dist > 200) {
            moveTowardsPlayer();
        }
        // shoot arrows at the player
        else if (dist <= 200 && dist >= 75) {
            shootArrow(dt);
        }
        // else do melee stuff
        else {
            doMeleeStuff(dt);
        }
    }
    
    @Override
    protected void wander(float dt) {
        super.wander(dt);
    }
    
    private void moveTowardsPlayer() {
        Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
        move(dirToPlayer);
    }
    
    private void shootArrow(float dt) {
        if (Envision.thePlayer == null) return;
        
//        var left = switch (facing) {
//        case LEFT, UP -> true;
//        default -> false;
//        };
        
        timeSinceLastFireball += dt;
        if (timeSinceLastFireball >= fireballDelay) {
            timeSinceLastFireball = 0;
            
            float diffX = (float) (midX - Envision.thePlayer.midX);
            float diffY = (float) (midY - Envision.thePlayer.midY);
            Vector3f dir = new Vector3f(diffX, diffY, 0.0f);
            dir.normalize();
            dir.mul(-1.0f);
            
            boolean bigDamage = ERandomUtil.roll(1, 1, 6);
            if (bigDamage) speak("Taste THIS!!", 1000, 1500);
            int modifier = (bigDamage) ? 3 : 1;
            
            var fb = new Arrow();
            fb.setBaseMeleeDamage(ERandomUtil.getRoll(3 * modifier, 7 * modifier));
            fb.setFiredDirection(dir);
            fb.setFiringEntity(this);
            fb.startX = midX - width * 0.5;
            fb.startY = midY - height * 0.5;
            if (bigDamage) fb.setSpeed(1000);
            
            world.addEntity(fb);
        }
    }
    
    private void doMeleeStuff(float dt) {
        Dimension_d testDim = getCollisionDims();
        Dimension_d pDims = Envision.thePlayer.getCollisionDims();
        
        if (testDim.partiallyContains(pDims)) {
            if (hit) {
                //System.out.println(System.currentTimeMillis() - timeSinceLastHit);
                timeSinceLastHit += dt;
                if (timeSinceLastHit >= 200) {
                    hit = false;
                }
            }
            else {
                hit = true;
                timeSinceLastHit = 0;
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

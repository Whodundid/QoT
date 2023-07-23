package qot.entities.enemies.archer;

import java.text.DecimalFormat;

import envision.Envision;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.Enemy;
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
        tex = EntityTextures.player;
        
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
        var p = Envision.thePlayer;
        if (p == null) {
            wander();
            return;
        }
        
        double dist = world.getDistance(this, p);
        this.headText = "" + new DecimalFormat("#.00").format(dist);
        
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
    
    private void wander() {
        if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
            waitTime = ERandomUtil.getRoll(randShort, randLong);
            moveTime = ERandomUtil.getRoll(randShort, 800l);
            waitDelay = ERandomUtil.getRoll(randShort, randLong);
            lastMove = System.currentTimeMillis();
            lastDir = ERandomUtil.randomDir(true);
        }
        
        if (System.currentTimeMillis() - lastMove >= moveTime) {
            move(lastDir);
        }
    }
    
    private void moveTowardsPlayer() {
        Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
        move(dirToPlayer);
    }
    
    private void shootArrow() {
        var left = switch (facing) {
        case LEFT, UP -> true;
        default -> false;
        };
        
        if (System.currentTimeMillis() - timeSinceLastFireball >= fireballDelay) {
            timeSinceLastFireball = System.currentTimeMillis();
            var fb = new Arrow();
            fb.worldX = (left) ? worldX : (int) (worldX + width / world.getTileWidth());
            fb.worldY = worldY;
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
                Envision.thePlayer.drainHealth(getBaseMeleeDamage());
            }
        }
        
        Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
        move(dirToPlayer);
    }
    
    @Override
    public int getInternalSaveID() {
        return EntityList.ARCHER.ID;
    }
    
}

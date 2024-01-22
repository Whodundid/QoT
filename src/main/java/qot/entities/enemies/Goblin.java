package qot.entities.enemies;

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

public class Goblin extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin");
		
		setBaseMeleeDamage(1);
		setMaxHealth(10);
		setHealth(10);
		setSpeed(32.0 * 3.0);
		agroRange = 200;
		
		init(posX, posY, 64, 64);
		sprite = new Sprite(EntityTextures.goblin);
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		setExperienceRewardedOnKill(25);
		
        // item on death
        
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, Items.random());
        itemOnDeath.setChance(10);
        
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
        double distToPlayer = world.getDistance(this, currentTarget);
        
        //check if distance to player is less than 200 pixels
        if (distToPlayer <= 200) {
            Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, currentTarget);
            //headText = (int) distToPlayer + " : " + dirToPlayer;
            
            Dimension_d testDim = getCollisionDims();
            Dimension_d pDims = currentTarget.getCollisionDims();
            
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
                    currentTarget.attackedBy(this, amount);
                }
            }
            move(dirToPlayer);
        }
        else {
            boolean shouldMove = ERandomUtil.roll(10, 0, 10);
            
            if (shouldMove) {
                Direction dir = ERandomUtil.randomDir();
                move(dir);
            }
        }
    }
	
	@Override
	public int getInternalSaveID() {
		return EntityList.GOBLIN.ID;
	}
	
}

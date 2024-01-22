package qot.entities.enemies;

import envision.engine.registry.types.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.Enemy;
import envision.game.entities.combat.EntityAttack;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class Whodundid extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid");
		init(posX, posY, 64, 64);
		sprite = new Sprite(EntityTextures.whodundid);
		lastDir = ERandomUtil.randomDir();
		
		setBaseMeleeDamage(3);
		setMaxHealth(20);
		setHealth(20);
		
		setCollisionBox(midX - 12, endY - 30, midX + 12, endY);
		setExperienceRewardedOnKill(50);
		
		randShort = 400l;
		randLong = 800l;
		
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
        
        double distToPlayer = world.getDistance(this, currentTarget);
        if (distToPlayer <= 300) {
            //headText = "" + health;
            
            Direction dirToPlayer = world.getDirectionTo(this, currentTarget);
            move(dirToPlayer);
        }
        else {
            //headText = "";
        }
    }
    
	@Override
	public int getInternalSaveID() {
		return EntityList.WHODUNDID.ID;
	}
	
}

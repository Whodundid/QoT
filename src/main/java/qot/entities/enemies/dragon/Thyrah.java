package qot.entities.enemies.dragon;

import envision.Envision;
import envision.engine.registry.types.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.Enemy;
import envision.game.entities.combat.EntityAttack;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.projectiles.Fireball;
import qot.items.Items;

public class Thyrah extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	private long timeSinceLastFireball;
	private long fireballDelay = 3000;
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyrah, the Dragon");
		init(posX, posY, 128, 128);
		sprite = new Sprite(EntityTextures.thyrah);
		
		setBaseMeleeDamage(10);
		setMaxHealth(40);
		setHealth(40);
		setCollisionBox(startX + 16, endY - 25, endX - 16, endY);
		setExperienceRewardedOnKill(500);
		
		randShort = 200l;
		randLong = 200l;
		
        // item on death
        
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, Items.random());
        itemOnDeath.setChance(1);
        
        addComponent(itemOnDeath);
	}

	@Override
	public void onLivingUpdate(float dt) {
	    lastMove += dt;
		if (lastMove >= waitTime + waitDelay) {
			waitTime = ERandomUtil.getRoll(randShort, randLong);
			//moveTime = RandomUtil.getRoll(randShort, 800l);
			//waitDelay = RandomUtil.getRoll(randShort, randLong);
			lastMove = 0;
			lastDir = ERandomUtil.randomDir(true);
		}
		
		if (lastMove >= moveTime) {
			move(lastDir);
		}
		
		var p = Envision.thePlayer;
		if (p == null) return;
		
		var left = switch (facing) {
		case LEFT, UP -> true;
		default -> false;
		};
		
		// shoot fireballs at the player
		if (world.getDistance(this, p) <= 150) {
		    timeSinceLastFireball += dt;
			if (timeSinceLastFireball >= fireballDelay) {
				timeSinceLastFireball = 0;
				var fb = new Fireball();
				fb.startX = (left) ? startX : endX;
				fb.startY = midY;
				
				world.addEntity(fb);
			}
		}
		
		Dimension_d testDim = getCollisionDims();
		Dimension_d pDims = Envision.thePlayer.getCollisionDims();
		
		if (testDim.partiallyContains(pDims)) {
			if (hit) {
				//System.out.println(System.currentTimeMillis() - timeSinceLastHit);
			    timeSinceLastHit += dt;
				if ((timeSinceLastHit) >= 200) {
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
		
		double distToPlayer = world.getDistance(this, Envision.thePlayer);
		if (distToPlayer <= 300) {
			headText = "" + health;
			
			Direction dirToPlayer = world.getDirectionTo(this, Envision.thePlayer);
			move(dirToPlayer);
		}
		else {
			headText = "";
		}
	}
	
	@Override
	public int getInternalSaveID() {
		return 4;
	}
	
}

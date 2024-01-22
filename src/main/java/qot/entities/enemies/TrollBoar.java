package qot.entities.enemies;

import envision.Envision;
import envision.engine.registry.types.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.Enemy;
import envision.game.entities.combat.EntityAttack;
import eutil.datatypes.points.Point2i;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.effects.SpeedEffect;
import qot.entities.EntityList;
import qot.items.Items;

public class TrollBoar extends Enemy {
	
	private long lastAttack;
	private long nextAttack;
	private Point2i lastPlayerPoint;
	private boolean hasPlayerMoved;
	private boolean doesWaitAttack;
	private boolean hasWaited;
	private long startWait = -1;
	private long waitAttackTime;
	private boolean passedMoveLogic;
	
	public static final SpeedEffect trollSpeed = new SpeedEffect("Troll Speed", 200);
	
	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar");
		init(posX, posY, 64, 64);
		sprite = new Sprite(EntityTextures.trollboar);
		setMaxHealth(20);
		setHealth(20);
		setExperienceRewardedOnKill(250);
		setBaseMeleeDamage(5);
		setSpeed(200);
		
		nextAttack = ERandomUtil.getRoll(200, 500);
		doesWaitAttack = ERandomUtil.randomBool();
		waitAttackTime = ERandomUtil.getRoll(600, 4000);
		
		//this.setHeadText(doesWaitAttack + " : " + waitAttackTime);
		
        // item on death
        
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, Items.random());
        itemOnDeath.setChance(1);
        
        addComponent(itemOnDeath);
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		provideSpeedAura();
		
		super.onLivingUpdate(dt);
	}
	
    @Override
    protected void runPassiveAI(float dt) {
        move(ERandomUtil.randomDir());
    }
    
    @Override
    protected void runAggressiveAI(float dt) {
        doLogic();
    }
	
	private boolean isPlayerClose() {
		var p = currentTarget;
		var w = Envision.theWorld;
		if (p == null || w == null) return false;
		
		if (!doesWaitAttack && lastPlayerPoint != null) {
			if (!passedMoveLogic) {
				if (p.worldX != lastPlayerPoint.x || p.worldY != lastPlayerPoint.y) {
					passedMoveLogic = true;
					//this.headText = "ATTACKING!";
					return true;
				}
			}
			else return true;
		}
		
		var dist = w.getDistance(this, p);
		return (dist >= 0 && dist < 150);
	}
	
	private void doLogic() {
		movementLogic();
		attackLogic();
	}
	
	private void movementLogic() {
		var cur = System.currentTimeMillis();
		var p = currentTarget;
		
		if (!passedMoveLogic) {
			if (doesWaitAttack) {
				if (!hasWaited) {
					if (startWait >= 0) {
						//this.headText = (cur - startWait) + " : " + waitAttackTime;
						if (cur - startWait >= waitAttackTime) {
							hasWaited = true;
							passedMoveLogic = true;
						}
					}
					else {
						startWait = cur;
						//this.headText += " : " + startWait;
					}
				}
			}
			else if (lastPlayerPoint != null) {
				//this.headText = "old[" + lastPlayerPoint.x + "," + lastPlayerPoint.y + "] [" + p.worldX + "," + p.worldY + "]"; 
				if (p.worldX == lastPlayerPoint.x && p.worldY == lastPlayerPoint.y) return;
				//this.headText = "ATTACKING!";
				passedMoveLogic = true;
			}
			else {
				lastPlayerPoint = new Point2i(p.worldX, p.worldY);
			}
		}
		else {
			var dir = world.getDirectionTo(this, currentTarget);
			if (dir != Direction.OUT) move(dir);			
		}
	}
	
	private void attackLogic() {
		//if (!passedMoveLogic) return;
		var cur = System.currentTimeMillis();
		
		if (cur - lastAttack >= nextAttack) {
			lastAttack = cur;
			determineNextAttack();
			attack();
		}
	}
	
	private long determineNextAttack() {
		return nextAttack = ERandomUtil.getRoll(200, 500);
	}
	
	private void attack() {
		var p = currentTarget;
		if (p == null) return;
		
		var dist = world.getDistance(this, p);
		if (dist < 200) {
		    p.drainMana(5);		    
		}
		
		var cb = getCollisionDims(); // collision box
		var phb = p.getCollisionDims(); // player hitbox
		
		if (cb.partiallyContains(phb)) {
			var bonus = 0;
			if (ERandomUtil.roll(0, 0, 2)) {
				bonus += 2;
				if (ERandomUtil.roll(0, 0, 2)) {
					bonus *= 2;
				}
			}
			else if (ERandomUtil.roll(0, 0, 2)) {
				bonus -= 5;
			}
			
			int amount = EntityAttack.calculateMeleeAttackDamage(this);
			amount += bonus;
            currentTarget.attackedBy(this, amount);
		}
	}
	
	private void provideSpeedAura() {
		 var closeEntities = world.getAllEntitiesWithinDistance(this, 50);
		 if (closeEntities == null) return;
		 
		 closeEntities = closeEntities.filter(e -> e instanceof Enemy);
		 
		 for (var e : closeEntities) {
			 if (e.isDead()) continue;
			 if (!e.activeEffectsTracker.hasEffect(trollSpeed)) {
			     e.activeEffectsTracker.addEffect(trollSpeed);
			 }
		 }
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.TROLLBOAR.ID;
	}
	
}

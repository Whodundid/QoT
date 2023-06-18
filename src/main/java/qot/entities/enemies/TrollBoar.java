package qot.entities.enemies;

import envision.Envision;
import envision.game.entities.Enemy;
import eutil.datatypes.points.Point2i;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;

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
	
	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar");
		init(posX, posY, 64, 64);
		tex = EntityTextures.trollboar;
		setMaxHealth(20);
		setHealth(20);
		setExperienceRewardedOnKill(250);
		setBaseMeleeDamage(5);
		this.speed = 200;
		
		nextAttack = ERandomUtil.getRoll(200, 500);
		doesWaitAttack = ERandomUtil.randomBool();
		waitAttackTime = ERandomUtil.getRoll(600, 4000);
		
		//this.setHeadText(doesWaitAttack + " : " + waitAttackTime);
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		provideSpeedAura();
		
		if (isPlayerClose()) doLogic();
		else move(ERandomUtil.randomDir());
	}
	
	private boolean isPlayerClose() {
		var p = Envision.thePlayer;
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
		var p = Envision.thePlayer;
		
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
			var dir = world.getDirectionTo(this, Envision.thePlayer);
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
		var p = Envision.thePlayer;
		if (p == null) return;
		
		p.drainMana(5);
		
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
			
			p.drainHealth(baseMeleeDamage + bonus);
		}
	}
	
	private void provideSpeedAura() {
		 var closeEntities = world.getAllEntitiesWithinDistance(this, 50);
		 if (closeEntities == null) return;
		 
		 closeEntities = closeEntities.filter(e -> e instanceof Enemy);
		 
		 for (var e : closeEntities) {
			 if (e.isDead()) continue;
			 e.activeEffectsTracker.put("SPEED_MODIFIER", 200.0);
		 }
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.TROLLBOAR.ID;
	}
	
}

package qot.entities.enemies;

import envision.Envision;
import envision.game.objects.entities.Enemy;
import envision.game.world.GameWorld;
import eutil.math.dimensions.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;

public class Whodundid extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid");
		init(posX, posY, 64, 64);
		tex = EntityTextures.whodundid;
		lastDir = ERandomUtil.randomDir();
		
		setBaseMeleeDamage(3);
		setMaxHealth(20);
		setHealth(20);
		
		setCollisionBox(startX + 6, endY - height / 2, endX - 6, endY);
		setExperienceRewardedOnKill(50);
		
		randShort = 400l;
		randLong = 800l;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
			waitTime = ERandomUtil.getRoll(randShort, randLong);
			moveTime = ERandomUtil.getRoll(randShort, randLong);
			waitDelay = ERandomUtil.getRoll(randShort, randLong);
			lastMove = System.currentTimeMillis();
			lastDir = ERandomUtil.randomDir();
		}
		
		if (System.currentTimeMillis() - lastMove >= moveTime) {
			move(lastDir);
		}
		
		if (Envision.thePlayer == null) return;
		
		EDimension testDim = getCollisionDims();
		EDimension pDims = Envision.thePlayer.getCollisionDims();
		
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
		
		double distToPlayer = ((GameWorld) world).getDistance(this, Envision.thePlayer);
		if (Envision.thePlayer != null && distToPlayer <= 300) {
			headText = "" + health;
			
			Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
			move(dirToPlayer);
		}
		else {
			headText = "";
		}
	}
	
	@Override
	public int getInternalSaveID() {
		return 2;
	}
	
}

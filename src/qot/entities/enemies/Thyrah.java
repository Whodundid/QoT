package qot.entities.enemies;

import envision.Envision;
import envision.game.objects.entities.Enemy;
import envision.game.world.GameWorld;
import eutil.math.dimensions.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;

public class Thyrah extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyrah, the Dragon");
		init(posX, posY, 128, 128);
		tex = EntityTextures.thyrah;
		
		setBaseMeleeDamage(10);
		setMaxHealth(40);
		setHealth(40);
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		setExperienceRewardedOnKill(500);
		
		randShort = 200l;
		randLong = 200l;
	}

	@Override
	public void onLivingUpdate(float dt) {
		if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
			waitTime = ERandomUtil.getRoll(randShort, randLong);
			//moveTime = RandomUtil.getRoll(randShort, 800l);
			//waitDelay = RandomUtil.getRoll(randShort, randLong);
			lastMove = System.currentTimeMillis();
			lastDir = ERandomUtil.randomDir(true);
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
		if (distToPlayer <= 300) {
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
		return 4;
	}
	
}

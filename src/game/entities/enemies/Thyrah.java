package game.entities.enemies;

import envision.gameEngine.gameObjects.entity.Enemy;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.world.gameWorld.GameWorld;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import game.QoT;
import game.assets.textures.entity.EntityTextures;

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
	public void onLivingUpdate() {
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
		
		if (QoT.thePlayer == null) return;
		
		EDimension testDim;
		EDimension pDims;
		
		{
			double cSX = startX + collisionBox.startX;
			double cSY = startY + collisionBox.startY;
			double cEX = endX - (width - collisionBox.endX);
			double cEY = endY - (height - collisionBox.endY);
			
			testDim = new EDimension(cSX, cSY, cEX, cEY);
		}
		
		{
			Entity e = QoT.thePlayer;
			double cSX = e.startX + e.collisionBox.startX;
			double cSY = e.startY + e.collisionBox.startY;
			double cEX = e.endX - (e.width - e.collisionBox.endX);
			double cEY = e.endY - (e.height - e.collisionBox.endY);
			
			pDims = new EDimension(cSX, cSY, cEX, cEY);
		}
		
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
				QoT.thePlayer.drainHealth(getBaseMeleeDamage());
			}
		}
		
		double distToPlayer = ((GameWorld) world).getDistance(this, QoT.thePlayer);
		if (distToPlayer <= 300) {
			headText = "" + health;
			
			Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, QoT.thePlayer);
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

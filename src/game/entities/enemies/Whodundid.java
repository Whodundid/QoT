package game.entities.enemies;

import envision.game.entity.Enemy;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import game.QoT;
import game.assets.textures.entity.EntityTextures;

public class Whodundid extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid");
		init(posX, posY, 64, 64);
		sprite = EntityTextures.whodundid;
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
	public void onLivingUpdate() {
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
		return 2;
	}
	
}

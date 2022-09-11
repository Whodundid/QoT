package game.entities.enemies;

import envision.gameEngine.gameObjects.entity.Enemy;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.world.gameWorld.GameWorld;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import game.QoT;
import game.assets.textures.entity.EntityTextures;

public class Goblin extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin");
		
		setBaseMeleeDamage(1);
		setMaxHealth(10);
		setHealth(10);
		
		init(posX, posY, 64, 64);
		sprite = EntityTextures.goblin;
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		setExperienceRewardedOnKill(25);
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
		
		double distToPlayer = ((GameWorld) world).getDistance(this, QoT.thePlayer);
		
		//check if distance to player is less than 200 pixels
		if (distToPlayer <= 200) {
			Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, QoT.thePlayer);
			//headText = (int) distToPlayer + " : " + dirToPlayer;
			
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
			
			headText = "" + getHealth();
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
			move(dirToPlayer);
		}
		else {
			boolean shouldMove = ERandomUtil.roll(10, 0, 10);
			headText = "";
			
			if (shouldMove) {
				Direction dir = ERandomUtil.randomDir();
				move(dir);
			}
		}
	}
	
	@Override
	public int getInternalSaveID() {
		return 1;
	}
	
}

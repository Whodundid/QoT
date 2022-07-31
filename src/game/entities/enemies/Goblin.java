package game.entities.enemies;

import assets.textures.EntityTextures;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.RandomUtil;
import game.entities.Entity;
import main.QoT;

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
	}
	
	@Override
	public void onLivingUpdate() {
		if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
			waitTime = RandomUtil.getRoll(randShort, randLong);
			moveTime = RandomUtil.getRoll(randShort, randLong);
			waitDelay = RandomUtil.getRoll(randShort, randLong);
			lastMove = System.currentTimeMillis();
			lastDir = RandomUtil.randomDir();
		}
		
		if (System.currentTimeMillis() - lastMove >= moveTime) {
			move(lastDir);
		}
		
		double distToPlayer = world.getDistance(this, QoT.thePlayer);
		
		//check if distance to player is less than 200 pixels
		if (distToPlayer <= 200) {
			Direction dirToPlayer = world.getDirectionTo(this, QoT.thePlayer);
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
			if (testDim.contains(pDims)) {
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
			boolean shouldMove = RandomUtil.roll(10, 0, 10);
			headText = "";
			
			if (shouldMove) {
				Direction dir = RandomUtil.randomDir();
				move(dir);
			}
		}
	}
	
	@Override
	public int getObjectID() {
		return 1;
	}
	
}
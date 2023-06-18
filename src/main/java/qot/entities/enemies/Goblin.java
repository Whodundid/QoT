package qot.entities.enemies;

import envision.Envision;
import envision.game.entities.Enemy;
import envision.game.world.GameWorld;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;

public class Goblin extends Enemy {
	
	private boolean hit = false;
	private long timeSinceLastHit;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin");
		
		setBaseMeleeDamage(1);
		setMaxHealth(10);
		setHealth(10);
		speed = 32 * 3;
		
		init(posX, posY, 64, 64);
		tex = EntityTextures.goblin;
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		setExperienceRewardedOnKill(25);
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
		
		if (Envision.thePlayer == null) {
			boolean shouldMove = ERandomUtil.roll(10, 0, 10);
			headText = "";
			
			if (shouldMove) {
				Direction dir = ERandomUtil.randomDir();
				move(dir);
			}
			
			return;
		}
		
		double distToPlayer = ((GameWorld) world).getDistance(this, Envision.thePlayer);
		
		//check if distance to player is less than 200 pixels
		if (Envision.thePlayer != null && distToPlayer <= 200) {
			Direction dirToPlayer = ((GameWorld) world).getDirectionTo(this, Envision.thePlayer);
			//headText = (int) distToPlayer + " : " + dirToPlayer;
			
			Dimension_d testDim = getCollisionDims();
			Dimension_d pDims = Envision.thePlayer.getCollisionDims();
			
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
					Envision.thePlayer.drainHealth(getBaseMeleeDamage());
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
		return EntityList.GOBLIN.ID;
	}
	
}

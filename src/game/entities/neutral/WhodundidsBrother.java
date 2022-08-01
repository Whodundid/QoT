package game.entities.neutral;

import assets.textures.EntityTextures;
import engine.animationHandler.AnimationHandler;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.RandomUtil;
import game.entities.Entity;
import game.entities.enemies.Enemy;
import main.QoT;

public class WhodundidsBrother extends Enemy {

	private boolean hit = false;
	private long timeSinceLastHit;
	
	public WhodundidsBrother() { this(0, 0); }
	public WhodundidsBrother(int posX, int posY) {
		super("Whodundid's Brother");
		init(posX, posY, 64, 64);
		
		setTexture(EntityTextures.whodundidsbrother);
		setBaseMeleeDamage(2);
		setMaxHealth(10);
		setHealth(10);
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		
		animationHandler = new AnimationHandler(this);
		var att1 = animationHandler.createAnimationSet(AnimationHandler.ATTACK_1);
		att1.addFrame(EntityTextures.whodundidsbrother);
		att1.addFrame(EntityTextures.whodundidsbrother2);
		att1.addFrame(EntityTextures.whodundidsbrother3);
		att1.addFrame(EntityTextures.whodundidsbrother4);
		att1.addFrame(EntityTextures.whodundidsbrother3);
		att1.addFrame(EntityTextures.whodundidsbrother2);
		randShort = 1500;
		randLong = 3000;
	}

	@Override
	public void onLivingUpdate() {
		animationHandler.onRenderTick();
		
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
		
		if (testDim.contains(pDims)) {
			if (hit) {
				//System.out.println(System.currentTimeMillis() - timeSinceLastHit);
				if ((System.currentTimeMillis() - timeSinceLastHit) >= 200) {
					hit = false;
				}
			}
			else {
				hit = true;
				animationHandler.playOnceIfNotAlreadyPlaying(AnimationHandler.ATTACK_1);
				timeSinceLastHit = System.currentTimeMillis();
				QoT.thePlayer.drainHealth(getBaseMeleeDamage());
			}
		}
		
		double distToPlayer = world.getDistance(this, QoT.thePlayer);
		if (distToPlayer <= 300) {
			headText = "" + health;
			
			Direction dirToPlayer = world.getDirectionTo(this, QoT.thePlayer);
			move(dirToPlayer);
		}
		else {
			headText = "";
		}
	}
	
	@Override
	public int getObjectID() {
		return 6;
	}
	
}

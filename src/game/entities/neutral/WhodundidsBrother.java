package game.entities.neutral;

import envision.game.animations.AnimationHandler;
import envision.game.entity.Enemy;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;
import eutil.math.EDimension;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import game.QoT;
import game.assets.textures.entity.EntityTextures;

public class WhodundidsBrother extends Enemy {

	private boolean hit = false;
	private long timeSinceLastHit;
	private long timeSinceLastBlink;
	private long delayTillNextBlink;
	
	public WhodundidsBrother() { this(0, 0); }
	public WhodundidsBrother(int posX, int posY) {
		super("Whodundid's Brother");
		init(posX, posY, 64, 64);
		
		setTexture(EntityTextures.whobro);
		setBaseMeleeDamage(2);
		setMaxHealth(10);
		setHealth(10);
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		setExperienceRewardedOnKill(50);
		
		randShort = 1500;
		randLong = 3000;
		
		delayTillNextBlink = ERandomUtil.getRoll(5000, 9000);
		
		animationHandler = new AnimationHandler(this);
		var att1 = animationHandler.createAnimationSet(AnimationHandler.ATTACK_1);
		att1.setUpdateInterval(15);
		att1.addFrame(EntityTextures.whobro);
		att1.addFrame(EntityTextures.whobro1);
		att1.addFrame(EntityTextures.whobro2);
		att1.addFrame(EntityTextures.whobro3);
		att1.addFrame(EntityTextures.whobro2);
		att1.addFrame(EntityTextures.whobro1);

		
		var idle1 = animationHandler.createAnimationSet(AnimationHandler.IDLE_ANIMATION_1);
		idle1.setUpdateInterval(10);
		idle1.addFrame(EntityTextures.whobro);
		idle1.addFrame(EntityTextures.whobro_blink0);
		idle1.addFrame(EntityTextures.whobro_blink1);
		idle1.addFrame(EntityTextures.whobro_blink2);
		idle1.addFrame(EntityTextures.whobro_blink1);
		idle1.addFrame(EntityTextures.whobro_blink0);
	}

	@Override
	public void onLivingUpdate() {
		animationHandler.onRenderTick();
		
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
		
		double distToPlayer = ((GameWorld) world).getDistance(this, QoT.thePlayer);
		if (distToPlayer <= 50) {
			animationHandler.playOnceIfNotAlreadyPlaying(AnimationHandler.ATTACK_1);
		}
		else if (!animationHandler.isAnimationLoaded()) {
			if (System.currentTimeMillis() - timeSinceLastBlink >= delayTillNextBlink) {
				timeSinceLastBlink = System.currentTimeMillis();
				delayTillNextBlink = ERandomUtil.getRoll(5000, 9000);
				animationHandler.playOnceIfNotAlreadyPlaying(AnimationHandler.IDLE_ANIMATION_1);
			}
		}
		
		if (testDim.partiallyContains(pDims)) {
			if (hit) {
				//System.out.println(System.currentTimeMillis() - timeSinceLastHit);
				if ((System.currentTimeMillis() - timeSinceLastHit) >= 600) {
					hit = false;
				}
			}
			else {
				hit = true;
				timeSinceLastHit = System.currentTimeMillis();
				QoT.thePlayer.drainHealth(getBaseMeleeDamage());
			}
		}
		
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
		return 6;
	}
	
}

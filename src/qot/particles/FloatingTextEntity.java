package qot.particles;

import envision.Envision;
import envision.game.entities.Entity;
import eutil.colors.EColors;

public class FloatingTextEntity extends Entity {

	protected String text;
	protected long creationTime;
	protected long timeToLive = 1500;
	protected boolean dieAfterSpecifiedTime = false;
	protected EColors drawColor = EColors.white;
	
	public FloatingTextEntity(int x, int y, Object text) {
		super("FloatingText: " + text);
		this.text = text + "";
		creationTime = System.currentTimeMillis();
		setInvincible(true);
		world = Envision.theWorld;
		setWorldPos(x, y);
	}
	
	public FloatingTextEntity(int x, int y, Object text, long timeToLive) {
		this(x, y, text);
		this.timeToLive = timeToLive;
		dieAfterSpecifiedTime = true;
	}
	
//	@Override
//	public void drawEntity(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
//		if (dieAfterSpecifiedTime) {
//			double moveAmount = 100; // 200 px
//			double moveY = ((System.currentTimeMillis() - creationTime) / (double) timeToLive) * moveAmount;
//			drawStringCS(text, x, y - moveY, drawColor);
//		}
//		else {
//			drawString(text, x, y, drawColor);
//		}
//	}
	
	public FloatingTextEntity setColor(EColors color) {
		drawColor = color;
		return this;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		if (!dieAfterSpecifiedTime) return;
		if (System.currentTimeMillis() - creationTime >= timeToLive) {
			kill();
			Envision.theWorld.getEntitiesInWorld().remove(this);
		}
	}

	@Override public int getInternalSaveID() { return -1; }
	
}

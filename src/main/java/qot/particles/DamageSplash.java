package qot.particles;

import envision.Envision;
import envision.game.effects.particles.Particle;
import eutil.colors.EColors;

public class DamageSplash extends Particle {
	
	double posX, posY;
	double damage;
	long creationTime;
	long timeOnScreen = 1500;
	
	public DamageSplash(double posXIn, double posYIn, double damageIn) {
		posX = posXIn;
		posY = posYIn;
		damage = damageIn;
		init(Envision.getActiveTopParent(), posX - 10, posY - 10, 20, 20);
	}
	
	@Override
	public void onFirstDraw() {
		creationTime = System.currentTimeMillis();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (System.currentTimeMillis() - creationTime >= timeOnScreen) {
			getParent().removeObject();
		}
		
		EColors color = (damage > 0) ? EColors.red : EColors.blue;
		EColors inner = (damage > 0) ? EColors.maroon : EColors.navy;
		
		drawFilledCircle(midX, midY, width * 2, 6, color);
		drawFilledCircle(midX, midY, width * 2 - 2, 6, inner);
		drawStringC(damage, midX, midY - 8);
		
		move(0, -1);
	}
	
}
package qot.particles;

import envision.Envision;
import envision.game.effects.particles.Particle;
import eutil.colors.EColors;

public class PointerPress extends Particle {
	
	int pressX, pressY;
	int rad = 15;
	long curTime = 0;
	
	public PointerPress(int mX, int mY) { this(mX, mY, 200); }
	public PointerPress(int mX, int mY, int startRadius) {
		pressX = mX;
		pressY = mY;
		rad = startRadius;
		
		init(Envision.getActiveTopParent(), mX - rad, mY - rad, rad * 2, rad * 2);
		curTime = System.currentTimeMillis();
		
		setClickable(false);
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		if (System.currentTimeMillis() - curTime >= 1) {
			rad -= 1;
			curTime = System.currentTimeMillis();
		}
		
		if (rad <= 0) {
			getParent().removeObject();
		}
		
		drawCircle(pressX, pressY, rad, 50, -EColors.rainbow());
	}
	
}

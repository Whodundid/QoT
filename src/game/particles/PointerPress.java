package game.particles;

import engine.QoT;
import engine.windowLib.windowTypes.WindowObject;
import eutil.colors.EColors;

public class PointerPress extends WindowObject {
	
	int pressX, pressY;
	int rad = 15;
	long curTime = 0;
	
	public PointerPress(int mX, int mY) { this(mX, mY, 200); }
	public PointerPress(int mX, int mY, int startRadius) {
		pressX = mX;
		pressY = mY;
		rad = startRadius;
		
		init(QoT.getActiveTopParent(), mX - rad, mY - rad, rad * 2, rad * 2);
		curTime = System.currentTimeMillis();
		
		setClickable(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (System.currentTimeMillis() - curTime >= 1) {
			rad -= 1;
			curTime = System.currentTimeMillis();
		}
		
		if (rad <= 0) {
			getParent().removeObject();
		}
		
		drawCircle(pressX, pressY, rad, 50, -EColors.rainbow());
		
		super.drawObject(mXIn, mYIn);
	}
	
}

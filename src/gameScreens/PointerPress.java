package gameScreens;

import eWindow.windowTypes.WindowObject;
import main.Game;
import util.renderUtil.EColors;

public class PointerPress extends WindowObject {
	
	int pressX, pressY;
	int rad = 15;
	long curTime = 0;
	
	public PointerPress(int mX, int mY) { this(mX, mY, 200); }
	public PointerPress(int mX, int mY, int startRadius) {
		pressX = mX;
		pressY = mY;
		rad = startRadius;
		
		init(Game.getGameRenderer(), mX - rad, mY - rad, rad * 2, rad * 2);
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
			getParent().removeObject(this);
		}
		
		drawCircle(pressX, pressY, rad, 50, -EColors.rainbow());
		
		super.drawObject(mXIn, mYIn);
	}
	
}

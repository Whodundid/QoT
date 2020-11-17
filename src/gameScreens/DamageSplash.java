package gameScreens;

import eWindow.windowTypes.WindowObject;
import main.Game;
import util.renderUtil.EColors;

public class DamageSplash extends WindowObject {
	
	double posX, posY;
	double damage;
	long creationTime;
	
	// Amount of time that the splash text stays on screen
	long timeOnScreen = 1500;
	
	public DamageSplash(double posXIn, double posYIn, double damageIn) {
		posX = posXIn;
		posY = posYIn;
		damage = damageIn;
		init(Game.getGameRenderer(), posX - 10, posY - 10, 20, 20);
		
		
	}
	
	@Override
	public void onFirstDraw() {
		creationTime = System.currentTimeMillis();
		super.onFirstDraw();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (System.currentTimeMillis() - creationTime >= timeOnScreen) {
			getParent().removeObject(this);
		}
		
		EColors color = (damage > 0) ? EColors.lred : EColors.blue;
		
		drawFilledCircle(midX, midY, width / 2, 6, color);
		drawString(damage, midX, posY);
		
		move(0, 1);
		
		super.drawObject(mXIn, mYIn);
	}
	
}

package gameScreens;

import eutil.colors.EColors;
import gameScreens.screenUtil.GameScreen;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowTypes.interfaces.IActionObject;

public class PlayerScreen extends GameScreen {
	
	WindowButton back, levelUp, stats, escape;
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		back = new WindowButton(this, 5, endY - 60, 100, 95, "Back");
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dgray);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) closeScreen();
	}
	
}

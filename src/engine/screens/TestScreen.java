package engine.screens;

import engine.screens.screenUtil.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;

public class TestScreen extends GameScreen {
	
	WindowButton b1;
	
	@Override
	public void initScreen() {
		
	}
	
	@Override
	public void initObjects() {
		b1 = new WindowButton(this, 15, 15, 50, 35, "T");
		
		addObject(b1);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(0, 0, 75, 95, EColors.steel);
		//drawString("T", 15, 65);
	}

	@Override
	public void onScreenClosed() {
		
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}

package qot.screens.gameplay.combat;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.entities.Entity;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CombatScreen extends GameScreen {
	
	EList<WindowObject> createdObjects = new EArrayList<>();
	Entity a, b;
	
	WindowButton run;
	WindowButton inventory, stats;
	
	public CombatScreen(Entity aIn, Entity bIn) {
		a = aIn;
		b = bIn;
	}
	
	@Override
	public void initScreen() {
		
		
		run = new WindowButton(this);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.vdgray);
		
		drawString("HAHAHAA", midX, midY);
		
		double scale = 3;
		double aWidth = a.width * scale;
		double bWidth = b.width * scale;
		double aHeight = a.height * scale;
		double bHeight = b.height * scale;
		double aX = (midX / 2) - (aWidth / 2);
		double bX = (midX / 2) + midX - (bWidth / 2);
		double aY = midY - (aHeight / 2);
		double bY = midY - (bHeight / 2);
		
		//int randX = RandomUtil.getRoll(-3, 3);
		//int randY = RandomUtil.getRoll(-3, 3);
		
		//bX += randX;
		//bY += randY;
		
		drawSprite(a.getSprite(), aX, aY, aWidth, aHeight, true);
		drawSprite(b.getSprite(), bX, bY, bWidth, bHeight, false);
	}
	
	@Override
	public void keyPressed(char typedChar, int keycode) {
		
	}
	
	@Override
	public void onScreenClosed() {
		Envision.unpause();
		Envision.unpauseWorldRender();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}

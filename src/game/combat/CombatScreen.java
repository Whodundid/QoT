package game.combat;

import engine.screens.screenUtil.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.entities.Entity;
import main.QoT;

public class CombatScreen extends GameScreen {
	
	EArrayList<WindowObject> createdObjects = new EArrayList();
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
		
		drawTexture(a.texture, aX, aY, aWidth, aHeight, true);
		drawTexture(b.texture, bX, bY, bWidth, bHeight, false);
	}
	
	@Override
	public void keyPressed(char typedChar, int keycode) {
		
	}
	
	@Override
	public void onScreenClosed() {
		QoT.unpause();
		QoT.unpauseWorldRender();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}

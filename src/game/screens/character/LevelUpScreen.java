package game.screens.character;

import engine.screenEngine.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.entities.Entity;
import main.QoT;

public class LevelUpScreen extends GameScreen {
	
	WindowButton back, upHealth, upDamage, upMana, stats, escape;
	Entity levelingEntity;
	
	public LevelUpScreen() { this(QoT.getPlayer()); }
	public LevelUpScreen(Entity theEntity) {
		levelingEntity = theEntity;
	}
	
	@Override public void initScreen() {}
	
	@Override
	public void initChildren() {
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
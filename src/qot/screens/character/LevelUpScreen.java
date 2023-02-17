package qot.screens.character;

import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.objects.entities.Entity;
import eutil.colors.EColors;
import qot.QoT;

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

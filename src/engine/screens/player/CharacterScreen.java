package engine.screens.player;

import engine.screens.screenUtil.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.entities.Entity;

public class CharacterScreen extends GameScreen {
	
	Entity theEntity;
	WindowButton back;
	WindowButton upHealth, upDamage, upMana;
	
	public CharacterScreen(Entity entIn) {
		theEntity = entIn;
	}
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		back = new WindowButton(this, 5, endY - 60, 100, 95, "Back");
		
		upHealth = new WindowButton(this, midX - 100, midY - 100, 40, 40);
		upDamage = new WindowButton(this, midX - 100, upHealth.endY + 10, 40, 40);
		upMana = new WindowButton(this, midX - 100, upDamage.endY + 10, 40, 40);
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
	
	private void levelHP() {}
	private void levelDam() {}
	private void levelMana() {}
	
}

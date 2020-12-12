package gameScreens.gameplay;

import assets.sounds.Songs;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.GameOverScreen;
import gameScreens.MainMenuScreen;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import util.renderUtil.EColors;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	StatusBar health, mana;
	
	@Override
	public void initScreen() {
		Songs.stopAllMusic();
		Songs.loop(Songs.battleTheme);
		
		setObjectName("Game Screen");
	}
	
	@Override
	public void initObjects() {
		windowObjects.clear();
		
		health = new StatusBar(this, 5, 5, 200, 30, 0, Game.getPlayer().getMaxHealth(), EColors.red);
		health.setCurrentValue(Game.getPlayer().getHealth());
		addObject(health);
		
		mana = new StatusBar(this, health.endX + 5, 5, 200, 30, 0, Game.getPlayer().getMaxMana(), EColors.blue);
		mana.setCurrentValue(Game.getPlayer().getMana());
		addObject(mana);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		if (Game.getPlayer().isDead()) {
			Game.displayScreen(new GameOverScreen());
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void onScreenClosed() {
		Game.displayScreen(new MainMenuScreen());
		Game.loadWorld(null);
		Songs.stopAllMusic();
	}
	
}

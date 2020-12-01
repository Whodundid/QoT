package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import screenParts.StatusBar;
import sound.Songs;
import util.renderUtil.EColors;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	StatusBar health, mana;
	WindowButton back;
	
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
		
		/*
		Game.getPlayer().setPosition(Game.getWidth() / 2 - 300, Game.getHeight() / 2);
		
		int random = NumUtil.getRoll(0, 3);
		switch (random) {
		case 0: addObject(new Goblin((int) midX, (int) midY)); break;
		case 1: addObject(new Whodundid((int) midX, (int) midY)); break;
		case 2: addObject(new TrollBoar((int) midX, (int) midY)); break;
		case 3: addObject(new Thyrah((int) midX, (int) midY)); break;
		}
		
		addObject(Game.getPlayer());
		*/
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Menu");
		
		addObject(back);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		//drawRect(EColors.dsteel);
		
		if (Game.getPlayer().isDead()) {
			Game.displayScreen(new GameOverScreen());
		}
		
		/*
		for (IWindowObject o : getObjects()) {
			if (o != Game.getPlayer() && o instanceof Entity e) {
				if (Game.getPlayer().getDimensions().contains(o.getDimensions())) {
					Game.displayScreen(new BattleScreen(this, Game.getPlayer(), e));
				}
			}
		}
		
		if (Keyboard.isWDown()) { Game.getPlayer().move(0, -1); }
		if (Keyboard.isADown()) { Game.getPlayer().move(-1, 0); }
		if (Keyboard.isSDown()) { Game.getPlayer().move(0, 1); }
		if (Keyboard.isDDown()) { Game.getPlayer().move(1, 0); }
		*/
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) {
			Game.loadWorld(null);
			closeScreen();
		}
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopAllMusic();
	}
	
}

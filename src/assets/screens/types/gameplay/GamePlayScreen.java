package assets.screens.types.gameplay;

import assets.screens.GameScreen;
import assets.screens.types.MainMenuScreen;
import assets.sounds.Songs;
import input.Keyboard;
import main.QoT;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import windowLib.windowObjects.basicObjects.WindowRect;
import windowLib.windowTypes.interfaces.IActionObject;
import world.GameWorld;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	GameWorld world;
	WindowRect topHud;
	WindowRect botHud; //don't know if actually want this one
	StatusBar health, mana;
	
	public GamePlayScreen() {
		super();
		screenHistory.push(new MainMenuScreen());
		world = QoT.getWorld();
	}
	
	@Override
	public void initScreen() {
		Songs.stopAllMusic();
		Songs.loop(Songs.battleTheme);
		
		setObjectName("Game Screen");
	}
	
	@Override
	public void initObjects() {
		windowObjects.clear();
		
		topHud = new WindowRect(this, 0, 0, QoT.getWidth(), 39, EColors.dgray);
		//addObject(topHud);
		
		health = new StatusBar(this, 5, 5, 200, 30, 0, QoT.getPlayer().getMaxHealth(), EColors.red);
		health.setCurrentValue(QoT.getPlayer().getHealth());
		//addObject(health);
		
		mana = new StatusBar(this, health.endX + 5, 5, 200, 30, 0, QoT.getPlayer().getMaxMana(), EColors.blue);
		mana.setCurrentValue(QoT.getPlayer().getMana());
		//addObject(mana);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isCtrlDown()) {
			world.setZoom(world.getZoom() + NumberUtil.round(Math.signum(change) * 0.25, 2));
			world.setZoom(NumberUtil.clamp(world.getZoom(), 0.25, 5));
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void onScreenClosed() {
		//Game.displayScreen(new MainMenuScreen());
		//Game.loadWorld(null);
		//Songs.stopAllMusic();
	}
	
}

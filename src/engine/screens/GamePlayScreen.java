package engine.screens;

import assets.sounds.Songs;
import engine.GameTopRenderer;
import engine.input.Keyboard;
import engine.screens.character.CharacterScreen;
import engine.screens.screenUtil.GameScreen;
import engine.soundEngine.SoundEngine;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.advancedObjects.StatusBar;
import engine.windowLib.windowObjects.basicObjects.WindowRect;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.entities.Player;
import main.QoT;
import world.GameWorld;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	Player player;
	GameWorld world;
	WindowRect topHud;
	WindowRect botHud; //don't know if actually want this one
	StatusBar health, mana;
	WindowButton character;
	
	public GamePlayScreen() {
		super();
		screenHistory.push(new MainMenuScreen());
		world = QoT.getWorld();
	}
	
	@Override
	public void initScreen() {
		SoundEngine.stopAll();
		SoundEngine.loop(Songs.battleTheme);
		
		setObjectName("Game Screen");
	}
	
	@Override
	public void initObjects() {
		windowObjects.clear();
		
		//assign player field
		player = QoT.getPlayer();
		
		//topHud = new WindowRect(this, 0, 0, QoT.getWidth(), 39, EColors.dgray);
		//addObject(topHud);
		
		health = new StatusBar(this, 5, 5, 200, 30, 0, player.getMaxHealth(), EColors.red);
		health.setBarValue(player.getHealth());
		addObject(health);
		
		mana = new StatusBar(this, health.endX + 5, 5, 200, 30, 0, player.getMaxMana(), EColors.blue);
		mana.setBarValue(player.getMana());
		addObject(mana);
		
		character = new WindowButton(this, mana.endX + 5, 5, 125, 30, "Stats");
		addObject(character);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		//top hud
		drawRect(0, 0, QoT.getWidth(), 39, EColors.dgray);
		drawRect(0, 39, QoT.getWidth(), 41, EColors.vdgray);
		
		//mouse pos
	}
	
	@Override
	public void onGameTick(long ticks) {
		if (!GameTopRenderer.isTopFocused()) {
			Player p = QoT.thePlayer;
			double moveSpeed = 1;
			if (Keyboard.isWDown()) p.move(0, -moveSpeed);
			if (Keyboard.isSDown()) p.move(0, moveSpeed);
			if (Keyboard.isADown()) p.move(-moveSpeed, 0);		
			if (Keyboard.isDDown()) p.move(moveSpeed, 0);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_TAB) openCharScreen();
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mouseScrolled(int change) {
		//if (Keyboard.isCtrlDown()) {
		//	world.setZoom(world.getZoom() + NumberUtil.round(Math.signum(change) * 0.25, 2));
		//	world.setZoom(NumberUtil.clamp(world.getZoom(), 0.25, 5));
		//}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == character) openCharScreen();
	}
	
	@Override
	public void onScreenClosed() {
		//Game.displayScreen(new MainMenuScreen());
		//QoT.loadWorld(null);
		//Songs.stopAllMusic();
	}
	
	private void openCharScreen() {
		QoT.displayScreen(new CharacterScreen(QoT.thePlayer), this);
	}
	
}

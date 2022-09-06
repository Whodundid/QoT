package game.screens.gameplay;

import envision.game.entity.Entity;
import envision.game.screens.GameScreen;
import envision.game.sounds.SoundEngine;
import envision.game.world.gameWorld.GameWorld;
import envision.inputHandlers.Keyboard;
import envision.topOverlay.GameTopScreen;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.basicObjects.WindowRect;
import envision.windowLib.windowObjects.basicObjects.WindowStatusBar;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import game.QoT;
import game.assets.sounds.Songs;
import game.assets.textures.item.ItemTextures;
import game.entities.player.QoT_Player;
import game.screens.character.CharacterScreen;
import game.screens.gameplay.combat.DeathScreen;
import game.screens.main.MainMenuScreen;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	QoT_Player player;
	GameWorld world;
	WindowRect topHud;
	WindowRect botHud; //don't know if actually want this one
	WindowStatusBar health, mana;
	WindowButton character;
	
	public int midDrawX, midDrawY; //the world coordinates at the center of the screen
	public int worldXPos, worldYPos; //the world coordinates under the mouse
	
	private boolean attacking = false;
	private long attackDrawStart;
	
	private GamePauseWindow pauseWindow;
	private boolean openPause = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GamePlayScreen() { this(false); }
	public GamePlayScreen(boolean openPauseOnStart) {
		super();
		screenHistory.push(new MainMenuScreen());
		world = QoT.getWorld();
		openPause = openPauseOnStart;
	}
	
	@Override
	public void initScreen() {
		SoundEngine.stopAll();
		SoundEngine.loop(Songs.battleTheme);
		
		setObjectName("Game Screen");
	}
	
	@Override
	public void initChildren() {
		getChildren().clear();
		
		//assign player field
		player = QoT.getPlayer();
		
		//topHud = new WindowRect(this, 0, 0, QoT.getWidth(), 39, EColors.dgray);
		//addObject(topHud);
		
		health = new WindowStatusBar(this, 5, 5, 200, 30, 0, player.getMaxHealth(), EColors.red);
		health.setBarValue(player.getHealth());
		addObject(health);
		
		mana = new WindowStatusBar(this, health.endX + 5, 5, 200, 30, 0, player.getMaxMana(), EColors.blue);
		mana.setBarValue(player.getMana());
		addObject(mana);
		
		character = new WindowButton(this, mana.endX + 5, 5, 125, 30, "Stats");
		
		if (openPause) openPauseWindow();
		
		//addObject(character);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		//top hud
		drawRect(0, 0, QoT.getWidth(), 39, EColors.lgray);
		drawRect(0, 39, QoT.getWidth(), 41, EColors.gray);
		//mouse pos
		
		if (world == null) {
			drawStringC("Null World!", midX, midY, EColors.lred);
			return;
		}
		
		health.setBarValue(QoT.thePlayer.getHealth());
		if (QoT.thePlayer != null && QoT.thePlayer.isDead()) {
			QoT.displayScreen(new DeathScreen());
		}
		
		if (attacking) {
			drawTexture(ItemTextures.iron_sword, midX - 32, midY - 32, 64, 64);
			
			if ((System.currentTimeMillis() - attackDrawStart) >= 100) {
				attacking = false;
			}
		}
		
		drawString("x" + world.getZoom(), QoT.getWidth() - 250, 12, EColors.dsteel);
		drawString("[" + player.worldX + ", " + player.worldY + "]", QoT.getWidth() - 900, 12, EColors.white);
		//drawString("[" + player.startX + ", " + player.startY + "]", QoT.getWidth() - 750, 12, EColors.white);
		//drawString("[" + world.getPixelWidth() + ", " + world.getPixelHeight() + "]", QoT.getWidth() - 550, 12, EColors.hotpink);
		
		//drawRect(midX, 0, midX + 1, endY, EColors.black);
		//drawRect(0, midY, endX, midY + 1, EColors.black);
	}
	
	@Override
	public void onGameTick(long ticks) {
		if (!GameTopScreen.isTopFocused()) {
			QoT_Player p = QoT.thePlayer;
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
		if (keyCode == Keyboard.KEY_ESC) openPauseWindow();
		
		if (keyCode == Keyboard.KEY_LEFT) QoT.thePlayer.move(-1, 0);
		if (keyCode == Keyboard.KEY_RIGHT) QoT.thePlayer.move(1, 0);
		if (keyCode == Keyboard.KEY_UP) QoT.thePlayer.move(0, -1);
		if (keyCode == Keyboard.KEY_DOWN) QoT.thePlayer.move(0, 1);
		
		//super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0 && !attacking) {
			attacking = true;
			attackDrawStart = System.currentTimeMillis();
			
			EArrayList<Entity> inRange = new EArrayList();
			for (var e : QoT.theWorld.getEntitiesInWorld()) {
				if (e == player) continue;
				if (QoT.theWorld.getDistance(e, player) < 50) inRange.add((Entity) e);
			}
			
			for (var e : inRange) {
				var damage = player.getBaseMeleeDamage();
				e.drainHealth(damage);
				//addObject(new DamageSplash(e.startX + e.midX, e.startY + e.midY, damage));
				if (e.isDead()) {
					QoT.thePlayer.getStats().addKilled(1);
					QoT.theWorld.getEntitiesInWorld().remove(e);
				}
			}
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z = 1.0;
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && world.getZoom() == 0.25) 	z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (world.getZoom() < 1.0) 		z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0) 						z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (world.getZoom() == 1.0) 		z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else 									z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = ENumUtil.round(world.getZoom() + z, 2);
			world.setZoom(z);
		}
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
	
	public void openPauseWindowIfNotOpen() {
		//System.out.println(this.properties.childrenToBeRemoved);
		if (pauseWindow != null && getChildren().contains(pauseWindow)) return;
		displayWindow(pauseWindow = new GamePauseWindow(this, 30, 30));
	}
	
	public void openPauseWindow() {
		if (getChildren().notContains(pauseWindow) || pauseWindow == null) {
			displayWindow(pauseWindow = new GamePauseWindow(this, 30, 30));
		}
		else {
			pauseWindow.close();
			pauseWindow = null;
		}
	}
	
}

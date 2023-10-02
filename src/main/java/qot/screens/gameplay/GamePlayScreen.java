package qot.screens.gameplay;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.screens.GameScreen;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowRect;
import envision.engine.windows.windowObjects.basicObjects.WindowStatusBar;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.effects.sounds.SoundEngine;
import envision.game.entities.Entity;
import envision.game.entities.player.Player;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.assets.sounds.Songs;
import qot.entities.player.QoT_Player;
import qot.screens.character.CharacterScreen;
import qot.screens.gameplay.combat.DeathScreen;
import qot.screens.main.MainMenuScreen;

//rabbit fish

public class GamePlayScreen extends GameScreen {
	
	Player player;
	IGameWorld world;
	WindowRect topHud;
	WindowRect botHud; //don't know if actually want this one
	WindowStatusBar health, mana;
	WindowButton<?> character;
	
	public int midDrawX, midDrawY; //the world coordinates at the center of the screen
	public int worldXPos, worldYPos; //the world coordinates under the mouse
	
	private GamePauseWindow pauseWindow;
	private boolean openPause = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GamePlayScreen() { this(false); }
	public GamePlayScreen(boolean openPauseOnStart) {
		super();
		//screenHistory.push(new MainMenuScreen());
		world = Envision.getWorld();
		openPause = openPauseOnStart;
		if (world != null) world.getCamera().setMinZoom(2);
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
		player = Envision.getPlayer();
		
		//topHud = new WindowRect(this, 0, 0, Envision.getWidth(), 39, EColors.dgray);
		//addObject(topHud);
		
		health = new WindowStatusBar(this, 5, 5, 200, 30, 0, player.getMaxHealth(), EColors.red);
		health.setBarValue(player.getHealth());
		addObject(health);
		
		mana = new WindowStatusBar(this, health.endX + 5, 5, 200, 30, 0, player.getMaxMana(), EColors.blue);
		mana.setBarValue(player.getMana());
		addObject(mana);
		
		character = new WindowButton<>(this, mana.endX + 5, 5, 125, 30, "Stats");
		
		if (openPause) openPauseWindow();
		
		//addObject(character);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		//top hud
		drawRect(0, 0, Envision.getWidth(), 39, EColors.lgray);
		drawRect(0, 39, Envision.getWidth(), 44, EColors.gray);
		//mouse pos
		
		if (world == null) {
			drawStringC("Null World!", midX, midY, EColors.lred);
			//keep trying to grab world instance
			world = Envision.theWorld;
			return;
		}
		else {
			//System.out.println(Envision.theWorld.getWorldName());
		}
		
		if (Envision.thePlayer != null) {
			health.setBarValue(Envision.thePlayer.getHealth());
			mana.setBarValue(Envision.thePlayer.getMana());
			
			if (Envision.thePlayer.isDead()) {
				Envision.displayScreen(new DeathScreen());
			}
		}
		
		drawString("x" + world.getCameraZoom(), Envision.getWidth() - 250.0, 12, EColors.dsteel);
		drawString("[" + player.worldX + ", " + player.worldY + "]", Envision.getWidth() - 900.0, 12, EColors.white);
		//drawString("[" + player.startX + ", " + player.startY + "]", Envision.getWidth() - 750, 12, EColors.white);
		//drawString("[" + world.getPixelWidth() + ", " + world.getPixelHeight() + "]", Envision.getWidth() - 550, 12, EColors.hotpink);
		
		//drawRect(midX, 0, midX + 1, endY, EColors.black);
		//drawRect(0, midY, endX, midY + 1, EColors.black);
		
		if (Envision.thePlayer != null) {
			Envision.thePlayer.getSpellbook().drawAbilities(this);
		}
	}
	
	@Override
	public void onGameTick(float dt) {
		if (!DeveloperDesktop.isOpen()) {
			QoT_Player p = (QoT_Player) Envision.thePlayer;
			
			double moveX = 0.0, moveY = 0.0;
			
			if (Keyboard.isADown()) moveX -= 1;
			if (Keyboard.isDDown()) moveX += 1;
			if (Keyboard.isWDown()) moveY -= 1;
			if (Keyboard.isSDown()) moveY += 1;
			
			if (Keyboard.isAnyKeyDown(Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_W, Keyboard.KEY_S)) {
				p.move(moveX, moveY);				
			}
			
//			double moveSpeed = 1;
//			if (Keyboard.isWDown()) p.move(0, -moveSpeed);
//			if (Keyboard.isSDown()) p.move(0, moveSpeed);
//			if (Keyboard.isADown()) p.move(-moveSpeed, 0);		
//			if (Keyboard.isDDown()) p.move(moveSpeed, 0);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_TAB) openCharScreen();
		if (keyCode == Keyboard.KEY_ESC) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LWIN)) Envision.displayScreen(new MainMenuScreen());
			else openPauseWindow();
		}
		
		if (keyCode == Keyboard.KEY_LEFT) Envision.thePlayer.movePixel(-1, 0);
		if (keyCode == Keyboard.KEY_RIGHT) Envision.thePlayer.movePixel(1, 0);
		if (keyCode == Keyboard.KEY_UP) Envision.thePlayer.movePixel(0, -1);
		if (keyCode == Keyboard.KEY_DOWN) Envision.thePlayer.movePixel(0, 1);
		
		if (keyCode == Keyboard.KEY_N) {
			Entity obj = world.getEntitiesInWorld().getRandom();
			world.getCamera().setFocusedObject(obj);
		}
		if (keyCode == Keyboard.KEY_M) {
			world.getCamera().setFocusedObject(Envision.thePlayer);
		}
		
		if (Envision.thePlayer != null) {
			Envision.thePlayer.onKeyPress(typedChar, keyCode);
		}
		
		if (keyCode == Keyboard.KEY_O) DebugSettings.drawEntityHitboxes = !DebugSettings.drawEntityHitboxes;
		if (keyCode == Keyboard.KEY_H) DebugSettings.drawEntityCollisionBoxes = !DebugSettings.drawEntityCollisionBoxes;
		if (keyCode == Keyboard.KEY_P) DebugSettings.drawEntityPositionTiles = !DebugSettings.drawEntityPositionTiles;
		
		//world.getWorldRenderer().keyPressed(typedChar, keyCode);
		
		//super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		double zoom = world.getCameraZoom();
		double drawDistX = world.getWorldRenderer().getDistX();
		double drawDistY = world.getWorldRenderer().getDistY();
		double camX = world.getCamera().getWorldX();
		double camY = world.getCamera().getWorldY();
		
		double tileDrawWidth = (world.getTileWidth() * zoom); //pixel width of each tile
		double tileDrawHeight = (world.getTileHeight() * zoom); //pixel height of each tile
		double drawAreaMidX = Envision.getWidth() / 2.0; //the middle x of the map draw area
		double drawAreaMidY = Envision.getHeight() / 2.0; //the middle y of the map draw area
		double mapDrawStartX = (drawAreaMidX - (drawDistX * tileDrawWidth) - (tileDrawWidth / 2.0)); //the left most x coordinate for map drawing
		double mapDrawStartY = (drawAreaMidY - (drawDistY * tileDrawHeight) - (tileDrawHeight / 2.0)); //the top most y coordinate for map drawing
		
        //converting to shorthand to reduce footprint
        double x = mapDrawStartX;
        double y = mapDrawStartY;
        double w = tileDrawWidth;
        double h = tileDrawHeight;
		
	    //The X world pixel under the mouse scaled by the render zoom
        int xWorldPixel = (int) Math.floor(mXIn - x - ((drawDistX - camX) * w));
        //The Y world pixel under the mouse scaled by the render zoom
        int yWorldPixel = (int) Math.floor(mYIn - y - ((drawDistY - camY) * h));
        
        //The X world pixel under the mouse with zoom scaling removed
        double xWorldPixelNoZoom = (int) (xWorldPixel / zoom);
        //The Y world pixel under the mouse with zoom scaling removed
        double yWorldPixelNoZoom = (int) (yWorldPixel / zoom);
        
        //The X coordinate world tile under the mouse
        //double xMouseTile = xWorldPixel / w;
        //The Y coordinate world tile under the mouse
        //double yMouseTile = yWorldPixel / h;
        
        EList<Entity> entitiesUnderMouse = new EArrayList<>(30);
        
        System.out.println((drawDistX - camX) + " : " + (drawDistY - camY));
        
        // garbage
        for (var e : world.getEntitiesInWorld()) {
            if (e.getDimensions().contains(xWorldPixelNoZoom, yWorldPixelNoZoom)) {
                entitiesUnderMouse.add(e);
            }
        }
        
        // filter the entities under mouse to the ones closest to the actual mouse press location
        for (var e : entitiesUnderMouse) {
            final var dims = e.getDimensions();
            final double midX = dims.midX;
            final double midY = dims.midY;
            final double dist = ENumUtil.distance(xWorldPixelNoZoom, yWorldPixelNoZoom, midX, midY);
            e.onMousePress(mXIn, mYIn, button);
            //System.out.println(dist + " : " + e);
        }
        
        if (Envision.thePlayer != null) {
            Envision.thePlayer.onMousePress(mXIn, mYIn, button);
        }
	}
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z;
		
		//if (Keyboard.isCtrlDown()) {
			if (c > 0 && world.getCameraZoom() == 0.25)      z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (world.getCameraZoom() < 1.0)            z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0)                                  z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (world.getCameraZoom() == 1.0)           z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else                                             z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = ENumUtil.round(world.getCameraZoom() + z, 2);
			world.setCameraZoom(z);
		//}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == character) openCharScreen();
	}
	
	@Override
	public void onScreenClosed() {
		//Game.displayScreen(new MainMenuScreen());
		//Envision.loadWorld(null);
		//Songs.stopAllMusic();
	}
	
	private void openCharScreen() {
		Envision.displayScreen(new CharacterScreen(Envision.thePlayer), this);
	}
	
	public void openPauseWindowIfNotOpen() {
		if (pauseWindow != null && getChildren().contains(pauseWindow) && !pauseWindow.isClosed()) return;
		pauseWindow = new GamePauseWindow(this, 30, 30);
		displayWindow(pauseWindow);
	}
	
	public void openPauseWindow() {
		if (getChildren().notContains(pauseWindow) || pauseWindow == null) {
		    pauseWindow = new GamePauseWindow(this, 30, 30);
			displayWindow(pauseWindow);
		}
		else {
			pauseWindow.close();
			pauseWindow = null;
		}
	}
	
	@Override
	public void onWorldLoaded() {
		this.world = Envision.theWorld;
	}
	
}

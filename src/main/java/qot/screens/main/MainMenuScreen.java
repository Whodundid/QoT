package qot.screens.main;

import java.io.File;

import org.joml.Vector2f;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.effects.sounds.SoundEngine;
import envision.game.manager.LevelManager;
import envision.game.world.GameWorld;
import envision.game.world.worldEditor.MapMenuScreen;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.random.ERandomUtil;
import eutil.timing.ESimpleTimer;
import qot.assets.sounds.Songs;
import qot.assets.textures.effects.EffectsTextures;
import qot.assets.textures.general.GeneralTextures;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.settings.QoTSettings;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	private boolean secret = false;
	
	private volatile GameWorld menuWorld = null;
	private int fromX, fromY;
	private int toX, toY;
	private Vector2f camMoveDir;
	
	private ESimpleTimer fadeInTimer = new ESimpleTimer(2000l);
	private ESimpleTimer nextWorldTimer = new ESimpleTimer(5000l);
	private ESimpleTimer fadeOutTimer = new ESimpleTimer(2000l);
	private ESimpleTimer fadeDelayTimer = new ESimpleTimer(500l);
	private ESimpleTimer waitForLoadTimer = new ESimpleTimer(300l);
	private long timeLoaded = -1;
	private long curTime = 0;
	private long timeBetweenMaps = 2000;
	private int lastGameTick = -1;
	private volatile boolean loaded = false;
	
	public MainMenuScreen() {
		super();
		aliases.add("main", "mainmenu", "titlescreen");
		secret = ERandomUtil.roll(1, 1, 1000);
	}
	
	@Override
	public void initScreen() {
		DebugSettings.drawFlatWalls = false;
		DebugSettings.drawTileGrid = false;
		
		Envision.thePlayer = null;
		
		if (Envision.theWorld != null) {
			Envision.loadLevel(null);
		}
		
		if (!SoundEngine.isPlaying(Songs.menu) && SoundEngine.getAllPlaying().size() == 1) {
			SoundEngine.stopAll();
		}
		SoundEngine.loopIfNotPlaying(Songs.menu);
		setObjectName("Main Menu Screen");
		
		if (QoTSettings.animatedMainMenu.getBoolean()) loadMenuWorld();
	}
	
	private void loadMenuWorld() {
	    loaded = false;
		menuWorld = null;
		Envision.theWorld = null;
		timeLoaded = -1;
		lastGameTick = -1;
		fadeInTimer.reset();
		fadeDelayTimer.reset();
		
		Envision.levelManager = new LevelManager();
		
		String[] maps = QoTSettings.getMenuWorldsDir().list();
		if (maps.length > 0) {
			int theMap = ERandomUtil.getRoll(0, maps.length - 1);
			String selected = maps[theMap];
			Runnable loader = () -> {
				if (secret) return;
				
				GameWorld world = menuWorld;
				world = new GameWorld(new File(QoTSettings.getMenuWorldsDir(), selected));
                var w = Envision.theWorld = world;
                Envision.levelManager.setActiveWorld(w);
                w.onLoad();
                w.getWorldRenderer().onWorldLoaded();
                Envision.levelManager.setCameraZoom(ERandomUtil.getRoll(2.5, 3));
                Envision.levelManager.setTime(ERandomUtil.getRoll(0, Envision.levelManager.getDayLength()));
                int ww = w.getWidth();
                int wh = w.getHeight();
                int lowerX = (ww / 2) - ww / 4;
                int lowerY = (wh / 2) - wh / 4;
                int upperX = (ww / 2) + ww / 4;
                int upperY = (wh / 2) + wh / 4;
                fromX = ERandomUtil.getRoll(lowerX, upperX);
                fromY = ERandomUtil.getRoll(lowerY, upperY);
                toX = ERandomUtil.getRoll(lowerX, upperX);
                toY = ERandomUtil.getRoll(lowerY, upperY);
                camMoveDir = new Vector2f(toX - fromX, toY - fromY);
                camMoveDir.normalize();
                Envision.levelManager.getCamera().setFocusedCoords(fromX, fromY);
				
				menuWorld = world;
				loaded = true;
			};
			Thread loaderThread = new Thread(loader);
			loaderThread.start();
		}
	}
	
	@Override
	public void initChildren() {
		//double w = ENumUtil.clamp(Envision.getWidth() / 4, 200, 320);
		double w = 290;
	    double x = 15;
		double y = 310;
		//double x = midX - w / 2;
		//double y = midY - 50;
		double h = 40;
		double gap = 5;
		
		newGame = new WindowButton(this, x, y, w, h, "New Game");
		loadGame = new WindowButton(this, x, y + (gap + h), w, h, "Load Game");
		options = new WindowButton(this, x, y + (gap + h) * 2, w, h, "Options");
		mapTest = new WindowButton(this, x, y + (gap + h) * 3, w, h, "Map Editor");
		closeGame = new WindowButton(this, x, endY - 80, w, h, "Quit Game");
		
		var tex = StoneFloorTextures.stone_pad;
		WindowButton.setTextures(tex, tex, newGame, loadGame, options, mapTest, closeGame);		
		
		addObject(newGame, loadGame, options, closeGame);
		addObject(mapTest);
	}
	
	@Override
	public void drawScreen(float dt, int mXIn, int mYIn) {
	    updateBackground(dt);
        
	    int sx = (int) newGame.startX - 5;
	    int sy = (int) newGame.startY - 20;
	    int ex = (int) newGame.endX + 5;
	    int ey = (int) closeGame.endY + 10;
	    
        //drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
        //drawRect(0, 0, ex + 10, endY, EColors.steel);
	    drawTexture(DungeonWallTextures.dung_wall_a, 0, 0, ex + 10, height);
        
        //for (int i = 0; i < )
        drawRect(sx, sy + 10, ex, ey, EColors.gray);
        drawTexture(DungeonWallTextures.dung_wall_a, sx, sy + 10, ex - sx, endY - (sy + 10));
        
        // logo background
        for (int i = 0; i < 12; i++) {
            int c = EColors.changeBrightness(EColors.skyblue.brightness(ERandomUtil.getRoll(230, 255)), 255 - i * 9);
            double offset = (i * 0.75) + 1;
            drawRect(sx + offset, 10 + offset, ex - offset, sy - offset, c);
        }
        
        Sprite staticSprite = EffectsTextures.static_effect_spritesheet.getRandom();
        drawSprite(staticSprite, sx, 10, (ex - sx), sy - 10, EColors.white.opacity(20));
        drawSprite(staticSprite, ex + 13, 0, endX - (ex + 13), height, EColors.white.opacity(10));
        
        // side fade
        drawRect(ex + 10, 0, ex + 13, endY, EColors.black);
        drawRect(ex + 13, 0, endX, endY, EColors.mgray.opacity(ERandomUtil.getRoll(70, 90)));
        
        for (int i = 0; i < 12; i++) {
            int c = EColors.changeOpacity(EColors.black.intVal, 100 - i * 9);
            int offset = (i * 9) + ERandomUtil.getRoll(1, 4);
            drawRect(ex + 13, 0, ex + 16 + offset, endY, c);
            drawRect(endX - offset, 0, endX, endY, c);
            drawRect(ex + 13, 0, endX, offset, c);
            drawRect(ex + 13, endY - offset, endX, endY, c);
        }
//        
//        //drawFilledEllipse(midX, midY - 220, 156, 106, 10, EColors.vdgray);
//        //drawFilledEllipse(midX, midY - 220, 150, 100, 10, EColors.rainbow());
//        //drawTexture(GeneralTextures.logo, midX - w / 2, midY - 320, w, 200);
//        
        drawTexture(GeneralTextures.logo, 5, 5, 290, 290);
        
        //draw copyright
        {
            var text = FontRenderer.COPYRIGHT + "Placeholder Studios";
            var sc = 0.7;
            var dx = newGame.endX - 98;
            var dy = height - FontRenderer.FH * sc - 3;
            drawStringCS(text, dx, dy, sc, sc, EColors.lgray);
        }
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		
		if (!isWorldLoaded()) return;
		
		double c = Math.signum(change);
		double z = 1.0;
		
		if (Keyboard.isCtrlDown()) {
		    var cam = Envision.levelManager.getCamera();
		    double zoom = cam.getZoom();
		    
			if (c > 0 && zoom == 0.25) 	z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (zoom < 1.0) 		z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0) 						z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (zoom == 1.0) 		z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else 									z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = ENumUtil.round(zoom + z, 2);
			cam.setZoom(z);
		}
	}
	
	@Override
	public void onGameTick(float dt) {
		if (isWorldLoaded()) {
			menuWorld.onGameTick(dt);
			
			final var cam = Envision.levelManager.getCamera();
			Vector2f curPos = new Vector2f((float) cam.getX(), (float) cam.getY());
			curPos.add(camMoveDir.mul(0.11f, new Vector2f()));
			cam.setFocusedPoint(curPos.x, curPos.y);
		}
	}
	
	private void updateBackground(float dt) {
	    boolean banana = false;
	    
        //draw underlying background image
        drawBackground(dt);
	    
		//check if delay timer has finished -- if so, start fade timer
		if (fadeDelayTimer.isFinished()) fadeInTimer.start();
		//check if the next world should start to be loaded
		if (nextWorldTimer.isFinished()) fadeOutTimer.start();
		//check if the fade out is complete -- in which case, load the new world
		if (fadeOutTimer.isFinished()) {
			secret = ERandomUtil.roll(1, 1, 1000);
			if (secret) nextWorldTimer.setDuration(1000l);
			else nextWorldTimer.setDuration(20000l);
			loadMenuWorld();
			return;
		};
		
		//boolean fadeInCounting = fadeInTimer.isStarted();
		//boolean fadeDelayCounting = fadeDelayTimer.isStarted();
		//boolean fadeOutCounting = fadeOutTimer.isStarted();
		boolean notLoadedAtTime = !isWorldLoaded();
		boolean anyCountingAtTime = ESimpleTimer.anyCounting(fadeInTimer, fadeDelayTimer, fadeOutTimer);
		
		//check to see whether or not the standard background should still draw
		if (!secret && notLoadedAtTime || anyCountingAtTime) {
			drawNullWorldBackground();
			banana = true;
			//start timer until the next world will be loaded
			if (fadeInTimer.isFinished()) nextWorldTimer.start();
		}
		else if (timeLoaded == -1) {
			timeLoaded = System.currentTimeMillis();
			fadeDelayTimer.start();
		}
		
		// this garbage works like 80% of the time, but it's still annoying af
		boolean force = !banana && !nextWorldTimer.isStarted();
        if (force) {
            drawNullWorldBackground();
        }
        
//		var s = new EStringBuilder();
//		s.a("[", !secret, ", ", notLoadedAtTime, ", (", anyCountingAtTime, ":", anyCounting, ")] ");//, " (", previouslyLoadedState, ":", force, ")] ");
//		s.a(fadeDelayCounting + " : ");
//		s.a(fadeInCounting + " : ");
//		s.a(fadeOutCounting + " : ");
//		s.a("[", banana, " : ", snarf, " : ", force, "]");
//		System.out.println(s);
//		drawString(s, 300, 300);
	}
	
	private boolean isWorldLoaded() {
	    return loaded && menuWorld != null && menuWorld.isFileLoaded();
	}
	
	private void drawBackground(float dt) {
		//if (fadeDelayTimer.isStarted()) return;
	    
		if (secret) {
			drawTexture(GeneralTextures.noscreens);
		}
		else if (isWorldLoaded()) {
			menuWorld.getWorldRenderer().onRenderTick(0f);
			drawRect(EColors.vdgray.opacity(30));
		    //drawRect(EColors.green.brightness(128));
		}
		else {
		    drawNullWorldBackground();
		}
	}
	
	private int opacity = 255;
	
	private void drawNullWorldBackground() {
		//GameTexture tex = StoneFloorTextures.stone_pad;
		GameTexture tex = StoneFloorTextures.dung_floor;
		double scale = 4.5;
		double tW = tex.getWidth() * scale;
		double tH = tex.getHeight() * scale;
		int numX = (int) Math.ceil(Envision.getWidth() / tW);
		int numY = (int) Math.ceil(Envision.getHeight() / tH);
		
		opacity = 255;
		
        if (ESimpleTimer.anyCounting(fadeInTimer, fadeOutTimer)) {
            //check fade in/out timers
            if (fadeInTimer.isStarted()) {
                var ratio = (255L * fadeInTimer.getRunTime()) / fadeInTimer.getDuration();
                opacity = 255 - (int) ratio;
            }
            else if (fadeOutTimer.isStarted()) {
                var ratio = (255L * fadeOutTimer.getRunTime()) / fadeOutTimer.getDuration();
                opacity = (int) ratio;
            }
        }
		
		opacity = ENumUtil.clamp(opacity, 0, 255);
		final var color = EColors.lgray.opacity(opacity);
		
		for (int i = 0; i < numY; i++) {
			for (int j = 0; j < numX; j++) {
				drawTexture(tex, tW * j, tH * i, tW, tH, color);
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newGame) 		newGame();
		if (object == loadGame)		load();
		if (object == options) 		options();
		if (object == closeGame) 	closeGame();
		if (object == mapTest) 		mapTest();
	}
	
	@Override
	public void onScreenResized() {
		super.onScreenResized();
		if (menuWorld != null) {
			menuWorld.getWorldRenderer().onWindowResized();
		}
	}
	
	//---------------------------------------------------
	
	private void newGame() {
	    fadeOutAndDisplayScreen(new WorldSelectScreen());
	}
	
	private void load() {
		
	}
	
	private void options() {
		fadeOutAndDisplayScreen(new OptionsScreen());
	}
	
	private void closeGame() {
		SoundEngine.stopAll();
		Envision.shutdown();
	}
	
	private void mapTest() {
	    fadeOutAndDisplayScreen(new MapMenuScreen());
	}
	
}

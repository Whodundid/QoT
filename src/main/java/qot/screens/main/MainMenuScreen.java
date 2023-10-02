package qot.screens.main;

import java.io.File;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.effects.sounds.SoundEngine;
import envision.game.world.GameWorld;
import envision.game.world.worldEditor.MapMenuScreen;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.random.ERandomUtil;
import eutil.timing.ESimpleTimer;
import qot.assets.sounds.Songs;
import qot.assets.textures.general.GeneralTextures;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.settings.QoTSettings;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	private boolean secret = false;
	
	private volatile GameWorld menuWorld = null;
	private ESimpleTimer fadeInTimer = new ESimpleTimer(1300l);
	private ESimpleTimer nextWorldTimer = new ESimpleTimer(5000l);
	private ESimpleTimer fadeOutTimer = new ESimpleTimer(1300l);
	private ESimpleTimer fadeDelayTimer = new ESimpleTimer(100l);
	private ESimpleTimer waitForLoadTimer = new ESimpleTimer(300l);
	private long timeLoaded = -1;
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
			Envision.loadWorld(null);
		}
		
		if (!SoundEngine.isPlaying(Songs.theme) && SoundEngine.getAllPlaying().size() == 1) {
			SoundEngine.stopAll();
		}
		SoundEngine.loopIfNotPlaying(Songs.theme);
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
		
		String[] maps = QoTSettings.getMenuWorldsDir().list();
		if (maps.length > 0) {
			int theMap = ERandomUtil.getRoll(0, maps.length - 1);
			String selected = maps[theMap];
			Runnable loader = () -> {
				if (secret) return;
				
				GameWorld world = menuWorld;
				world = new GameWorld(new File(QoTSettings.getMenuWorldsDir(), selected));
                var w = Envision.theWorld = world;
                w.getWorldRenderer().onWorldLoaded();
                w.setCameraZoom(ERandomUtil.getRoll(2.5, 3));
                w.setTime(ERandomUtil.getRoll(0, w.getDayLength()));
                int ww = w.getWidth();
                int wh = w.getHeight();
                int lowerX = (ww / 2) - ww / 4;
                int lowerY = (wh / 2) - wh / 4;
                int upperX = (ww / 2) + ww / 4;
                int upperY = (wh / 2) + wh / 4;
                int wx = ERandomUtil.getRoll(lowerX, upperX);
                int wy = ERandomUtil.getRoll(lowerY, upperY);
                w.getCamera().setFocusedCoords(wx, wy);
				
				menuWorld = world;
				loaded = true;
			};
			Thread loaderThread = new Thread(loader);
			loaderThread.start();
		}
	}
	
	@Override
	public void initChildren() {
		double w = ENumUtil.clamp(Envision.getWidth() / 4, 200, 320);
		double x = midX - w / 2;
		double y = midY - 50;
		double h = 40;
		double gap = 5;
		
		newGame = new WindowButton(this, x, y, w, h, "New Game");
		loadGame = new WindowButton(this, x, y + (gap + h), w, h, "Load Game");
		options = new WindowButton(this, x, y + (gap + h) * 2, w, h, "Options");
		closeGame = new WindowButton(this, x, y + (gap + h) * 3, w, h, "Quit Game");
		
		var tex = StoneFloorTextures.stone_pad;
		WindowButton.setTextures(tex, tex, newGame, loadGame, options, closeGame);
		
		mapTest = new WindowButton(this, 10, 10, w, h, "Map Editor");
		
		addObject(newGame, loadGame, options, closeGame);
		addObject(mapTest);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
	    updateBackground();
        
        drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
        double w = 250;
        
        drawFilledEllipse(midX, midY - 220, 156, 106, 10, EColors.vdgray);
        drawFilledEllipse(midX, midY - 220, 150, 100, 10, EColors.rainbow());
        drawTexture(GeneralTextures.logo, midX - w / 2, midY - 320, w, 200);
        
        //draw copyright
        {
            var text = FontRenderer.COPYRIGHT + "Placeholder Studios";
            var sc = 0.7;
            var dx = 3;
            var dy = height - FontRenderer.FH * sc - 3;
            drawStringS(text, dx, dy, sc, sc, EColors.lgray);
        }
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		
		if (!isWorldLoaded()) return;
		
		double c = Math.signum(change);
		double z = 1.0;
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && menuWorld.getCameraZoom() == 0.25) 	z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (menuWorld.getCameraZoom() < 1.0) 		z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0) 						z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (menuWorld.getCameraZoom() == 1.0) 		z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else 									z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = ENumUtil.round(menuWorld.getCameraZoom() + z, 2);
			menuWorld.setCameraZoom(z);
		}
	}
	
	@Override
	public void onGameTick(float dt) {
		if (isWorldLoaded()) {
			menuWorld.onGameTick(dt);
		}
	}
	
	private void updateBackground() {
	    boolean banana = false;
	    
        //draw underlying background image
        drawBackground();
	    
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
	
	private void drawBackground() {
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

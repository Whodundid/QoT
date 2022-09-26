package game.screens.main;

import java.io.File;

import envision.debug.DebugSettings;
import envision.gameEngine.effects.sounds.SoundEngine;
import envision.gameEngine.gameSystems.screens.GameScreen;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldEditor.MapMenuScreen;
import envision.inputHandlers.Keyboard;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.renderEngine.textureSystem.GameTexture;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.random.ERandomUtil;
import eutil.timing.ETimer;
import game.QoT;
import game.assets.sounds.Songs;
import game.assets.textures.general.GeneralTextures;
import game.assets.textures.world.floors.stone.StoneFloorTextures;
import game.settings.QoTSettings;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	private boolean secret = false;
	
	private volatile GameWorld menuWorld = null;
	private ETimer fadeInTimer = new ETimer(1300l);
	private ETimer nextWorldTimer = new ETimer(20000l);
	private ETimer fadeOutTimer = new ETimer(1300l);
	private ETimer fadeDelayTimer = new ETimer(100l);
	private long timeLoaded = -1;
	private int lastGameTick = -1;
	
	public MainMenuScreen() {
		super();
		aliases.add("main", "mainmenu", "titlescreen");
		secret = ERandomUtil.roll(1, 1, 1000);
	}
	
	@Override
	public void initScreen() {
		DebugSettings.drawFlatWalls = false;
		DebugSettings.drawTileGrid = false;
		
		if (QoT.theWorld != null) {
			QoT.loadWorld(null);
		}
		
		if (!SoundEngine.isPlaying(Songs.theme) && SoundEngine.getAllPlaying().size() == 1) {
			SoundEngine.stopAll();
		}
		SoundEngine.loopIfNotPlaying(Songs.theme);
		setObjectName("Main Menu Screen");
		
		if (QoTSettings.animatedMainMenu.get()) loadMenuWorld();
	}
	
	private void loadMenuWorld() {
		menuWorld = null;
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
				
				menuWorld = new GameWorld(new File(QoTSettings.getMenuWorldsDir(), selected));
				var w = QoT.theWorld = menuWorld;
				w.getWorldRenderer().onWorldLoaded();
				w.setCameraZoom(ERandomUtil.getRoll(2.5, 3));
				w.setUnderground(ERandomUtil.roll(1, 1, 10));
				int ww = w.getWidth();
				int wh = w.getHeight();
				int lowerX = (ww / 2) - ww / 4;
				int lowerY = (wh / 2) - wh / 4;
				int upperX = (ww / 2) + ww / 4;
				int upperY = (wh / 2) + wh / 4;
				int wx = ERandomUtil.getRoll(lowerX, upperX);
				int wy = ERandomUtil.getRoll(lowerY, upperY);
				w.getCamera().setFocusedCoords(wx, wy);
				
			};
			Thread loaderThread = new Thread(loader);
			loaderThread.start();
		}
	}
	
	@Override
	public void initChildren() {
		double w = ENumUtil.clamp(QoT.getWidth() / 4, 200, 320);
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
			var text = FontRenderer.COPYRIGHT + "Placeholder Industries";
			var sc = 0.7;
			var dx = 3;
			var dy = height - FontRenderer.FH * sc - 3;
			drawStringS(text, dx, dy, sc, sc, EColors.lgray);
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		
		if (menuWorld == null) return;
		
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
	public void onGameTick(long ticks) {
		if (menuWorld != null) {
			menuWorld.onGameTick();
		}
	}
	
	private void updateBackground() {
		//draw underlying background image
		drawBackground();
		
		//check if delay timer has finished -- if so, start fade timer
		if (fadeDelayTimer.check()) fadeInTimer.start();
		//check if the next world should start to be loaded
		if (nextWorldTimer.check()) fadeOutTimer.start();
		//check if the fade out is complete -- in which case, load the new world
		if (fadeOutTimer.check()) {
			secret = ERandomUtil.roll(1, 1, 1000);
			if (secret) nextWorldTimer.setDuration(1000l);
			else nextWorldTimer.setDuration(20000l);
			loadMenuWorld();
			return;
		};
		
		//check to see whether or not the standard background should still draw
		if (!secret && (menuWorld == null || !menuWorld.isFileLoaded()) ||
			ETimer.anyCounting(fadeInTimer, fadeDelayTimer, fadeOutTimer))
		{
			drawNullWorldBackground();
			//start timer until the next world will be loaded
			if (fadeInTimer.check()) nextWorldTimer.start();
		}
		else if (timeLoaded == -1) {
			timeLoaded = System.currentTimeMillis();
			fadeDelayTimer.start();
		}
	}
	
	private void drawBackground() {
		if (fadeDelayTimer.isStarted()) return;
		
		if (secret) {
			drawTexture(GeneralTextures.noscreens);
		}
		else if (menuWorld != null && menuWorld.isFileLoaded()) {
			menuWorld.getWorldRenderer().onRenderTick();
			drawRect(EColors.vdgray.opacity(30));
		}
	}
	
	private void drawNullWorldBackground() {
		//GameTexture tex = StoneFloorTextures.stone_pad;
		GameTexture tex = StoneFloorTextures.dung_floor;
		double scale = 4.5;
		double tW = tex.getWidth() * scale;
		double tH = tex.getHeight() * scale;
		int numX = (int) Math.ceil(QoT.getWidth() / tW);
		int numY = (int) Math.ceil(QoT.getHeight() / tH);
		
		int opacity = 255;
		
		//check fade in/out timers
		if (fadeInTimer.isStarted()) {
			var ratio = ((255L * fadeInTimer.getRunTime()) / fadeInTimer.getDuration());
			opacity = 255 - (int) ratio;
		}
		else if (fadeOutTimer.isStarted()) {
			var ratio = ((255L * fadeOutTimer.getRunTime()) / fadeOutTimer.getDuration());
			opacity = (int) ratio;
		}
		
		opacity = ENumUtil.clamp(opacity, 0, 255);
		
		for (int i = 0; i < numY; i++) {
			for (int j = 0; j < numX; j++) {
				drawTexture(tex, tW * j, tH * i, tW, tH, EColors.lgray.opacity(opacity));
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
		QoT.displayScreen(new WorldSelectScreen(), this);
	}
	
	private void load() {
		
	}
	
	private void options() {
		QoT.displayScreen(new OptionsScreen(), this);
	}
	
	private void closeGame() {
		SoundEngine.stopAll();
		QoT.stopGame();
	}
	
	private void mapTest() {
		QoT.displayScreen(new MapMenuScreen(), this);
	}
	
}

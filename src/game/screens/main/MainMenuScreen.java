package game.screens.main;

import assets.sounds.Songs;
import assets.textures.general.GeneralTextures;
import engine.screenEngine.GameScreen;
import engine.soundEngine.SoundEngine;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.NumberUtil;
import main.QoT;
import world.mapEditor.MapMenuScreen;
import world.worldTiles.categories.DungeonTiles;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	
	public MainMenuScreen() {
		super();
		aliases.add("main", "mainmenu", "titlescreen");
	}
	
	@Override
	public void initScreen() {
		if (QoT.theWorld != null) {
			QoT.loadWorld(null);
		}
		
		if (!SoundEngine.isPlaying(Songs.zarus) && SoundEngine.getAllPlaying().size() == 1) {
			SoundEngine.stopAll();
		}
		SoundEngine.loopIfNotPlaying(Songs.zarus);
		setObjectName("Main Menu Screen");
	}
	
	@Override
	public void initChildren() {
		double w = NumberUtil.clamp(QoT.getWidth() / 4, 200, 320);
		double x = midX - w / 2;
		double y = midY - 50;
		double h = 40;
		double gap = 5;
		
		newGame = new WindowButton(this, x, y, w, h, "New Game");
		loadGame = new WindowButton(this, x, y + (gap + h), w, h, "Load Game");
		options = new WindowButton(this, x, y + (gap + h) * 2, w, h, "Options");
		closeGame = new WindowButton(this, x, y + (gap + h) * 3, w, h, "Quit Game");
		
		mapTest = new WindowButton(this, 10, 10, w, h, "Map Editor");
		
		addObject(newGame, loadGame, options, closeGame);
		addObject(mapTest);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawTexture(DungeonTiles.dungFloor.getTexture());
		//drawRect(EColors.rainbow());
		//GLSettings.pushMatrix();
		//double r = 350;
		//double degree = (System.currentTimeMillis() % (360 * 16)) / 16;
		//double dX = midX + r * Math.cos(degree * (Math.PI / 180));
		//double dY = (midY - 50) + r * Math.sin(degree * (Math.PI / 180));
		//drawStringC("QUEST OF THYRAH", dX, dY, EColors.black);
		//GLSettings.popMatrix();
		drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
		double w = 250;
		
		drawFilledEllipse(midX, midY - 220, 156, 106, 10, EColors.vdgray);
		drawFilledEllipse(midX, midY - 220, 150, 100, 10, EColors.rainbow());
		drawTexture(GeneralTextures.logo, midX - w / 2, midY - 320, w, 200);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newGame) 		newGame();
		if (object == loadGame)		load();
		if (object == options) 		options();
		if (object == closeGame) 	closeGame();
		if (object == mapTest) 		mapTest();
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

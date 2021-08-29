package gameScreens;

import assets.sounds.Songs;
import assets.textures.GeneralTextures;
import eutil.colors.EColors;
import eutil.math.NumberUtil;
import gameScreens.screenUtil.GameScreen;
import main.QoT;
import mapEditor.MapMenuScreen;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowTypes.interfaces.IActionObject;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	
	@Override
	public void initScreen() {
		if (!Songs.isSongPlaying(Songs.theme)) { Songs.stopAllMusic(); }
		Songs.loopIfNotPlaying(Songs.theme);
		setObjectName("Main Menu Screen");
	}
	
	@Override
	public void initObjects() {
		double w = NumberUtil.clamp(QoT.getWidth() / 4, 200, 320);
		double x = midX - w / 2;
		double y = midY - 150;
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
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.rainbow());
		//GLSettings.pushMatrix();
		//double r = 350;
		//double degree = (System.currentTimeMillis() % (360 * 16)) / 16;
		//double dX = midX + r * Math.cos(degree * (Math.PI / 180));
		//double dY = (midY - 50) + r * Math.sin(degree * (Math.PI / 180));
		//drawStringC("QUEST OF THYRAH", dX, dY, EColors.black);
		//GLSettings.popMatrix();
		drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
		double w = 250;
		
		drawTexture(midX - w / 2, midY - 400, w, 200, GeneralTextures.logo);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newGame) { newGame(); }
		if (object == loadGame) { load(); }
		if (object == options) { options(); }
		if (object == closeGame) { closeGame(); }
		if (object == mapTest) { mapTest(); }
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
		QoT.stopGame();
	}
	
	private void mapTest() {
		QoT.displayScreen(new MapMenuScreen(), this);
	}
	
}

package gameScreens;

import assets.sounds.Songs;
import gameSystems.screenSystem.GameScreen;
import main.Game;
import mapEditor.MapMenuScreen;
import mathUtil.NumberUtil;
import renderUtil.EColors;
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
		double w = NumberUtil.clamp(Game.getWidth() / 4, 200, 320);
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
		drawRect(EColors.skyblue);
		drawStringC("QUEST OF THYRAH", midX, newGame.startY / 2);
		drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
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
		Game.displayScreen(new WorldSelectScreen(), this);
	}
	
	private void load() {
		
	}
	
	private void options() {
		Game.displayScreen(new OptionsScreen(), this);
	}
	
	private void closeGame() {
		Game.stopGame();
	}
	
	private void mapTest() {
		Game.displayScreen(new MapMenuScreen(), this);
	}
	
}

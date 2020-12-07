package gameScreens;

import assets.sounds.Songs;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.MapMenuScreen;
import gameSystems.gameRenderer.GameScreen;
import java.io.File;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	WindowButton mapTest;
	
	@Override
	public void initScreen() {
		if (!(getPreviousScreen() instanceof OptionsScreen)) {
			Songs.loop(Songs.theme);
		}
		setObjectName("Main Menu Screen");
	}
	
	@Override
	public void initObjects() {
		double w = NumUtil.clamp(Game.getWidth() / 4, 200, 320);
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
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.skyblue);
		//drawLine(midX - 1, 0, midX, Game.getHeight(), 1, EColors.black);
		drawStringC("QUEST OF THYRAH", midX, newGame.startY / 2);
		
		drawRect(newGame.startX - 10, newGame.startY - 10, newGame.endX + 10, closeGame.endY + 10, EColors.dsteel);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newGame) {
			//Game.setPlayer(new Player("Demmeonockhc"));
			//GameWorld w = new GameWorld(new File("test.twld"));
			//w.addEntity(Game.thePlayer);
			//Game.loadWorld(w);
			//Game.displayScreen(new GamePlayScreen(), this);
			Game.displayScreen(new WorldRenderTest(new File("test.twld")), this);
			Songs.stopSong(Songs.theme);
		}
		
		if (object == loadGame) {
			
		}
		
		if (object == options) {
			Game.displayScreen(new OptionsScreen(), this);
		}
		
		if (object == closeGame) {
			Game.stopGame();
		}
		
		if (object == mapTest) {
			Game.displayScreen(new MapMenuScreen(), this);
		}
	}

	@Override public void onScreenClosed() {}
	
}

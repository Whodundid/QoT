package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import sound.Songs;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class MainMenuScreen extends GameScreen {
	
	WindowButton newGame, loadGame, options, closeGame;
	
	@Override
	public void initScreen() {
		Songs.loop(Songs.theme);
		setObjectName("Main Menu Screen");
	}
	
	@Override
	public void initObjects() {
		double w = NumUtil.clamp(Game.getWidth() / 4, 200, 390);
		double x = midX - w / 2;
		double y = midY - 150;
		double h = 40;
		double gap = 5;
		
		newGame = new WindowButton(this, x, y, w, h, "New Game");
		loadGame = new WindowButton(this, x, y + (gap + h), w, h, "Load Game");
		options = new WindowButton(this, x, y + (gap + h) * 2, w, h, "Options");
		closeGame = new WindowButton(this, x, y + (gap + h) * 3, w, h, "Quit Game");
		
		addObject(newGame, loadGame, options, closeGame);
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
			Game.setPlayer(new Player("Demmeonockhc"));
			Game.displayScreen(new GamePlayScreen());
		}
		
		if (object == loadGame) {
			
		}
		
		if (object == options) {
			
		}
		
		if (object == closeGame) {
			Game.stopGame();
		}
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopSong(Songs.theme);
	}
	
}

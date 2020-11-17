package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import sound.Songs;

public class GameOverScreen extends GameScreen {
	
	WindowButton startOver, close;
	
	@Override
	public void initScreen() {
		Songs.playSong(Songs.lithinburg).loop();
	}
	
	@Override
	public void initObjects() {
		startOver = new WindowButton(this, midX - 100, midY - 30, 200, 50, "Start Over");
		close = new WindowButton(this, midX - 100, midY + 25, 200, 50, "Close Game");
		
		addObject(startOver, close);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawStringC("You have died!", midX, midY - 300);
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopSong(Songs.lithinburg);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == startOver) {
			Game.displayScreen(new TestScreen(null));
		}
		
		if (object == close) {
			Game.stopGame();
		}
	}
	
}

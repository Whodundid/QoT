package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import sound.Songs;

public class GameOverScreen extends GameScreen {
	
	Player p;
	WindowButton startOver, close;
	
	public GameOverScreen() { this(null); }
	public GameOverScreen(Player in) {
		super();
		p = in;
	}
	
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
		
		if (p != null) {
			drawStringC("You killed: " + p.getBackgroundStats().getEnemiesKilled(), midX, midY - 150);
		}
	}
	
	@Override
	public void onClosed() {
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

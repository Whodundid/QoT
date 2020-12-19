package gameScreens;

import assets.entities.player.Player;
import assets.sounds.Songs;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.screenSystem.GameScreen;
import main.Game;

public class GameOverScreen extends GameScreen {
	
	Player p;
	WindowButton menu, load, close;
	
	public GameOverScreen() { this(null); }
	public GameOverScreen(Player in) {
		super();
		p = in;
	}
	
	@Override
	public void initScreen() {
		Songs.loop(Songs.lithinburg);
		setObjectName("Game Over");
	}
	
	@Override
	public void initObjects() {
		menu = new WindowButton(this, midX - 100, midY - 30, 200, 40, "Main Menu");
		load = new WindowButton(this, midX - 100, midY + 15, 200, 40, "Load Game");
		close = new WindowButton(this, midX - 100, midY + 60, 200, 40, "Close Game");
		
		addObject(menu, load, close);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawStringC("You have died!", midX, midY - 300);
		
		if (p != null) {
			drawStringC("You killed: " + p.getStats().getEnemiesKilled(), midX, midY - 150);
		}
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopSong(Songs.lithinburg);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == menu) {
			Game.displayScreen(new MainMenuScreen());
		}
		
		if (object == load) {
			
		}
		
		if (object == close) {
			Game.stopGame();
		}
	}
	
}

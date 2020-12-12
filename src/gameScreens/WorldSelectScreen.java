package gameScreens;

import assets.entities.player.Player;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.gameplay.GamePlayScreen;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.mapSystem.GameWorld;
import java.io.File;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class WorldSelectScreen extends GameScreen {
	
	WindowButton defaultWorld, lastEditor;
	WindowButton back;
	
	String error;
	
	@Override
	public void initScreen() {
		error = null;
	}
	
	@Override
	public void initObjects() {
		int w = NumUtil.clamp(Game.getWidth() / 4, 150, 350);
		defaultWorld = new WindowButton(this, midX - w / 2, midY - 200, w, 45, "Default Map");
		lastEditor = new WindowButton(this, midX - w / 2, defaultWorld.endY + 5, w, 45, Game.settings.lastMap.get());
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		addObject(defaultWorld);
		if (!lastEditor.getString().isBlank() && !lastEditor.getString().isEmpty()) { addObject(lastEditor); }
		addObject(back);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawRect(midX - 252, midY - 252, midX + 252, midY - 48, EColors.black);
		drawRect(midX - 250, midY - 250, midX + 250, midY - 50, EColors.steel);
		
		drawStringC("Chose a World", midX, (midY - 250) / 2);
		
		if (error != null) {
			drawStringC(error, midX, endY - 100);
		}
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == defaultWorld) {
			Player p = Game.setPlayer(new Player("Test"));
			GameWorld w = Game.loadWorld(new GameWorld(new File("test.twld")));
			w.addEntity(p);
			Game.displayScreen(new GamePlayScreen());
			//Game.displayScreen(new WorldRenderTest(new File("test.twld")), this);
		}
		
		if (object == lastEditor) {
			String last = Game.settings.lastMap.get();
			if (last.isBlank() || last.isEmpty()) { error = "There is no last editor map!"; }
			else {
				Player p = Game.setPlayer(new Player("Test"));
				GameWorld w = Game.loadWorld(new GameWorld(new File(last)));
				w.addEntity(p);
				Game.displayScreen(new GamePlayScreen());
				//Game.displayScreen(new WorldRenderTest(new File(last)), this);
			}
		}
		
		if (object == back) { closeScreen(); }
	}
	
}

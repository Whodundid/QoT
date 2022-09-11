package game.screens.gameplay.combat;

import envision.gameEngine.effects.sounds.SoundEngine;
import envision.gameEngine.gameSystems.screens.GameScreen;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.misc.Rotation;
import game.QoT;
import game.assets.sounds.Songs;
import game.screens.main.MainMenuScreen;

public class DeathScreen extends GameScreen {
	
	private WindowButton mainMenu;
	
	@Override
	public void initScreen() {
		SoundEngine.stopAll();
		SoundEngine.loopIfNotPlaying(Songs.lithinburg);
		QoT.loadWorld(null);
	}
	
	@Override
	public void initChildren() {
		double w = 200;
		double h = 40;
		
		mainMenu = new WindowButton(this, midX - (w / 2), midY + (midY / 4) + (h / 2), w, h, "Main Manu");
		
		IActionObject.setActionReceiver(this, mainMenu);
		
		addObject(mainMenu);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		var tex = QoT.thePlayer.getTexture();
		double w = 128;
		double h = 128;
		drawTexture(tex, midX - (w / 2), midY + (h / 4) - (h / 2), w, h, Rotation.LEFT);
		
		drawStringC("YOU DIED!", midX, midY - (midY / 4) - (FontRenderer.FONT_HEIGHT * 2), EColors.lred);
		String killed = "You killed " + QoT.thePlayer.getStats().getEnemiesKilled() + " monsters!";
		drawStringC(killed, midX, midY - (midY / 4), EColors.seafoam);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == mainMenu) QoT.displayScreen(new MainMenuScreen());
	}

}

package engine.screens;

import assets.sounds.Songs;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.screens.screenUtil.GameScreen;
import engine.soundEngine.SoundEngine;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.misc.Rotation;
import main.QoT;

public class DeathScreen extends GameScreen {
	
	private WindowButton mainMenu;
	
	@Override
	public void initScreen() {
		SoundEngine.stopAll();
		SoundEngine.loopIfNotPlaying(Songs.lithinburg);
		QoT.loadWorld(null);
	}
	
	@Override
	public void initObjects() {
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

package qot.screens.gameplay.combat;

import envision.Envision;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.effects.sounds.SoundEngine;
import eutil.colors.EColors;
import eutil.misc.Rotation;
import qot.assets.sounds.Songs;
import qot.screens.main.MainMenuScreen;

public class DeathScreen extends GameScreen {
	
	private WindowButton mainMenu;
	
	@Override
	public void initScreen() {
		SoundEngine.stopAll();
		SoundEngine.loopIfNotPlaying(Songs.lithinburg);
		Envision.loadWorld(null);
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
		if (Envision.thePlayer == null) return;
		
		var tex = Envision.thePlayer.getTexture();
		double w = 128;
		double h = 128;
		drawTexture(tex, midX - (w / 2), midY + (h / 4) - (h / 2), w, h, Rotation.LEFT);
		
		drawStringC("YOU DIED!", midX, midY - (midY / 4) - (FontRenderer.FONT_HEIGHT * 2), EColors.lred);
		String killed = "You killed " + Envision.thePlayer.getStats().getEnemiesKilled() + " monsters!";
		drawStringC(killed, midX, midY - (midY / 4), EColors.seafoam);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == mainMenu) Envision.displayScreen(new MainMenuScreen());
	}

}

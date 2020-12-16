package gameScreens;

import assets.sounds.Songs;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowSlider;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.screenSystem.GameScreen;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class OptionsScreen extends GameScreen {
	
	WindowButton back;
	WindowSlider volumeSlider;
	
	@Override
	public void initScreen() {
		
	}
	
	@Override
	public void initObjects() {
		double w = NumUtil.clamp(Game.getWidth() / 4, 150, 390);
		double x = midX - w / 2;
		
		back = new WindowButton(this, x, endY - 100, w, 40, "Back");
		volumeSlider = new WindowSlider(this, midX - 150, midY - 100, 300, 35, 0, 100, false);
		volumeSlider.setUseIntegers(true);
		volumeSlider.setSliderValue(Game.settings.musicVolume.get());
		
		addObject(back, volumeSlider);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		drawStringC("Music Volume", midX, volumeSlider.startY - 30);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) {
			closeScreen();
		}
		
		if (object == volumeSlider) {
			double val = volumeSlider.getSliderValue();
			
			Game.settings.musicVolume.set((int) val);
			Game.saveConfig();
			
			if (val == 0) { Songs.stopAllMusic(); }
			else {
				Songs.loopIfNotPlaying(Songs.varthums);
				Songs.getAllCurrentlyPlaying().forEach(s -> s.setVolume((float) (val * 0.0009f)));
			}
		}
	}

	@Override public void onScreenClosed() {}
	
}

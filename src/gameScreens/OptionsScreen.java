package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowObjects.actionObjects.WindowCheckBox;
import eWindow.windowObjects.actionObjects.WindowSlider;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.gameRenderer.GameScreen;
import main.Game;
import sound.Songs;
import util.mathUtil.NumUtil;
import util.miscUtil.SysUtil;
import util.renderUtil.EColors;

public class OptionsScreen extends GameScreen {
	
	WindowButton back;
	WindowCheckBox playMusic;
	WindowSlider volumeSlider;
	
	WindowButton testButton, test2;
	
	@Override
	public void initScreen() {
		
	}
	
	@Override
	public void initObjects() {
		double w = NumUtil.clamp(Game.getWidth() / 4, 150, 390);
		double x = midX - w / 2;
		
		back = new WindowButton(this, x, endY - 100, w, 40, "Back");
		playMusic = new WindowCheckBox(this, midX, midY, 100, 100, Game.settings.playMusic.get());
		
		testButton = new WindowButton(this, 50, 50, 200, 50, "test1");
		test2 = new WindowButton(this, testButton.endX + 5, 50, 200, 50, "test2");
		
		addObject(back, playMusic);
		addObject(testButton, test2);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) {
			closeScreen();
		}
		
		if (object == playMusic) {
			Game.settings.playMusic.toggle();
			Game.saveConfig();
			
			if (!Game.settings.playMusic.get()) { Songs.stopAllMusic(); }
			else { Songs.loop(Songs.zarus); }
		}
		
		//load
		if (object == testButton) {
			System.out.println(SysUtil.getOS());
		}
		
		//save
		if (object == test2) {
			
		}
	}

	@Override public void onScreenClosed() {}
	
}

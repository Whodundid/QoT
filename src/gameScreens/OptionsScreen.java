package gameScreens;

import assets.sounds.Songs;
import eutil.colors.EColors;
import eutil.math.NumberUtil;
import gameScreens.screenUtil.GameScreen;
import input.Mouse;
import main.QoT;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowObjects.actionObjects.WindowCheckBox;
import windowLib.windowObjects.actionObjects.WindowSlider;
import windowLib.windowObjects.actionObjects.WindowTextField;
import windowLib.windowTypes.interfaces.IActionObject;

public class OptionsScreen extends GameScreen {
	
	WindowButton back;
	WindowSlider volumeSlider, fpsSlider;
	WindowTextField upsInput;
	WindowCheckBox vSync;
	private boolean changed = false;
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		double w = NumberUtil.clamp(QoT.getWidth() / 4, 150, 390);
		double x = midX - w / 2;
		
		back = new WindowButton(this, x, endY - 100, w, 40, "Back");
		volumeSlider = new WindowSlider(this, midX - 150, midY - 200, 300, 35, 0, 100, false);
		volumeSlider.setUseIntegers(true);
		volumeSlider.setSliderValue(QoT.settings.musicVolume.get());
		
		fpsSlider = new WindowSlider(this, volumeSlider.startX, volumeSlider.endY + 60, 300, 35, 30, 300, false);
		fpsSlider.setUseIntegers(true);
		fpsSlider.setSliderValue(QoT.getTargetFPS());
		
		upsInput = new WindowTextField(this, fpsSlider.startX, fpsSlider.endY + 60, 75, 35);
		upsInput.setText(QoT.getTargetUPS());
		
		addObject(back, volumeSlider, fpsSlider, upsInput);
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		drawString("Music Volume", volumeSlider.startX, volumeSlider.startY - 30);
		drawString("FPS", fpsSlider.startX, fpsSlider.startY - 30);
		drawString("Game Updates Per Second", upsInput.startX, upsInput.startY - 30);
		
		if (changed && !Mouse.isLeftDown()) {
			QoT.saveConfig();
			changed = false;
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) { back(); }
		if (object == volumeSlider) { volumeSlider(); }
		if (object == fpsSlider) { fpsSlider(); }
		if (object == upsInput) { upsInput(); }
		if (object == vSync) { vSync(); }
	}
	
	//---------------------------------------------
	
	private void back() {
		closeScreen();
	}
	
	private void volumeSlider() {
		double val = volumeSlider.getSliderValue();
		
		QoT.settings.musicVolume.set((int) val);
		changed = true;
		
		if (val == 0) { Songs.stopAllMusic(); }
		else {
			Songs.loopIfNotPlaying(Songs.theme);
			Songs.getAllCurrentlyPlaying().forEach(s -> s.setVolume((float) (val * 0.0009f)));
		}
	}
	
	private void fpsSlider() {
		double val = fpsSlider.getSliderValue();
		
		QoT.settings.targetFPS.set((int) val);
		QoT.setTargetFPS((int) val);
		changed = true;
	}
	
	private void upsInput() {
		String input = upsInput.getText();
		
		if (input == null) { upsInput.setText(QoT.getTargetUPS()); return; }
		if (input.isBlank() || input.isEmpty()) return;
		
		try {
			int ups = Integer.parseInt(input);
			
			ups = NumberUtil.clamp(ups, 30, 300);
			upsInput.setText(ups);
			
			QoT.settings.targetUPS.set((int) ups);
			QoT.setTargetUPS(ups);
			changed = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			upsInput.setText(QoT.getTargetUPS());
		}
	}
	
	private void vSync() {
		
	}
	
}

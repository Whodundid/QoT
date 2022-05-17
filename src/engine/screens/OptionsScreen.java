package engine.screens;

import engine.QoT;
import engine.input.Mouse;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.screens.screenUtil.GameScreen;
import engine.settings.QoT_Settings;
import engine.soundEngine.SoundEngine;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowCheckBox;
import engine.windowLib.windowObjects.actionObjects.WindowSlider;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.NumberUtil;

public class OptionsScreen extends GameScreen {
	
	WindowButton fullscreen;
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
		volumeSlider.setSliderValue(QoT_Settings.musicVolume.get());
		
		fpsSlider = new WindowSlider(this, volumeSlider.startX, volumeSlider.endY + 60, 300, 35, 30, 300, false);
		fpsSlider.setUseIntegers(true);
		fpsSlider.setSliderValue(QoT.getTargetFPS());
		
		upsInput = new WindowTextField(this, fpsSlider.startX, fpsSlider.endY + 60, 75, 35);
		upsInput.setText(QoT.getTargetUPS());
		
		boolean fs = QoT_Settings.fullscreen.get();
		String fText = (fs) ? "Disable FullScreen" : "Enable FullScreen";
		double fsW = FontRenderer.getStringWidth("Disable FullScreen") + 40;
		fullscreen = new WindowButton(this, upsInput.startX, upsInput.endY + 20, fsW, 35, fText);
		
		boolean vS = QoT_Settings.vsync.get();
		vSync = new WindowCheckBox(this, fullscreen.startX, fullscreen.endY + 20, 50, 50, vS);
		
		addObject(back, volumeSlider, fpsSlider, upsInput, fullscreen, vSync);
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		drawString("Music Volume", volumeSlider.startX, volumeSlider.startY - 30);
		drawString("FPS", fpsSlider.startX, fpsSlider.startY - 30);
		drawString("Game Updates Per Second", upsInput.startX, upsInput.startY - 30);
		drawString("V-Sync", vSync.endX + 10, vSync.midY - FontRenderer.FONT_HEIGHT / 2);
		
		if (changed && !Mouse.isLeftDown()) {
			QoT.saveConfig();
			changed = false;
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) 			back();
		if (object == volumeSlider) 	volumeSlider();
		if (object == fpsSlider) 		fpsSlider();
		if (object == upsInput) 		upsInput();
		if (object == vSync) 			vSync();
		if (object == fullscreen)		fullscreen();
	}
	
	//---------------------------------------------
	
	private void back() {
		closeScreen();
	}
	
	private void volumeSlider() {
		double val = volumeSlider.getSliderValue();
		SoundEngine.setMusicVolume(val);
		
		if (val == 0) SoundEngine.stopAll();
		else {
			if (SoundEngine.anyPlaying()) {
				//Songs.loop(Songs.theme);
			}
			SoundEngine.setCurrentTrackVolume(val);
		}
	}
	
	private void fpsSlider() {
		double val = fpsSlider.getSliderValue();
		
		QoT_Settings.targetFPS.set((int) val);
		QoT.setTargetFPS((int) val);
		changed = true;
	}
	
	private void upsInput() {
		String input = upsInput.getText();
		
		if (input == null) {
			upsInput.setText(QoT.getTargetUPS());
			return;
		}
		
		if (input.isBlank() || input.isEmpty()) return;
		
		try {
			int ups = Integer.parseInt(input);
			ups = NumberUtil.clamp(ups, 30, 300);
			upsInput.setText(ups);
			
			QoT_Settings.targetUPS.set((int) ups);
			QoT.setTargetUPS(ups);
			changed = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			upsInput.setText(QoT.getTargetUPS());
		}
	}
	
	private void vSync() {
		QoT.setVSync(!QoT_Settings.vsync.get());
	}
	
	private void fullscreen() {
		QoT.setFullScreen(!QoT_Settings.fullscreen.get());
	}
	
}

package envision.engine.screens;

import envision.engine.windows.windowUtil.EGui;
import eutil.colors.EColors;

public class ScreenFadeManager extends EGui {
	
	private GameScreen screen;
	private GameScreen previous;
	private boolean init;
	
	/** The point at which the screen will start fading. */
	private long screenFadeStart;
	/** The amount of time it takes for the screen to fade in/out. (measured in milliseconds) */
	private long screenFadeDuration = 1500;
	/** True if the screen is actively fading. */
	private boolean isFading = false;
	/** True if fading in. */
	private boolean isFadingIn = false;
	/** True if screen fading should happen on load/close. */
	private boolean performFade = true;
	
	public void fadeOutScreen(GameScreen screenIn, GameScreen previousIn, boolean initIn) {
		screen = screenIn;
		previous = previousIn;
		init = initIn;
	}
	
	public void onDraw() {
		if (!isFading) return;
		
		long curRatio = ((255L * (System.currentTimeMillis() - screenFadeStart)) / screenFadeDuration);
		
		if (isFadingIn) drawRect(EColors.changeOpacity(EColors.rainbow(), (255 - (int) curRatio)));
		else drawRect(EColors.changeOpacity(EColors.rainbow(), (int) curRatio));
		
		if (System.currentTimeMillis() - screenFadeStart >= screenFadeDuration) {
			isFading = false;
			if (!isFadingIn) closeScreen();
		}
	}
	
	//a b o
	//0 0 0
	//1 0 1
	//0 1 1
	//1 1 0
	
	private void closeScreen() {
		
	}
	
}

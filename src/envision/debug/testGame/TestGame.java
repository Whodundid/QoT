package envision.debug.testGame;

import envision.Envision;
import envision.EnvisionGame;
import envision.debug.testStuff.TestWindow;
import envision.engine.screens.ScreenLevel;
import envision.engine.windows.windowUtil.ObjectPosition;

public class TestGame implements EnvisionGame {
	
	@Override
	public void onPostEngineLoad() {
		//Envision.displayScreen(new MainMenuScreen());
		//Envision.displayScreen(new GameScreen());
		
		Envision.displayWindow(ScreenLevel.SCREEN, new TestWindow(200, 200, 300, 300), ObjectPosition.EXISTING_OBJECT_INDENT);
		Envision.displayWindow(ScreenLevel.SCREEN, new TestWindow(800, 200, 300, 300), ObjectPosition.EXISTING_OBJECT_INDENT);
	}
	
}

package envision.debug.testGame;

import envision.Envision;
import envision.debug.testStuff.TestWindow;
import envision.engine.loader.EnvisionGame;
import envision.engine.screens.ScreenLevel;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.launcher.EnvisionGameLauncher;
import envision.launcher.LauncherSettings;

public class TestGame extends EnvisionGame {
	
    private TestGameSettings settings = new TestGameSettings();
    private TestGameWorldCreator worldCreator = new TestGameWorldCreator(this);
    
	@Override
	public void onPostEngineLoad() {
		//Envision.displayScreen(new MainMenuScreen());
		//Envision.displayScreen(new GameScreen());
		
		Envision.displayWindow(ScreenLevel.SCREEN, new TestWindow(200, 200, 300, 300), ObjectPosition.EXISTING_OBJECT_INDENT);
		Envision.displayWindow(ScreenLevel.SCREEN, new TestWindow(800, 200, 300, 300), ObjectPosition.EXISTING_OBJECT_INDENT);
	}

    @Override
    public String getGameName() { return "Test Game"; }

    @Override
    public String getGameVersionString() { return "0.0.0"; }

    @Override
    public TestGameSettings getGameSettings() { return settings; }

    @Override
    public TestGameWorldCreator getWorldCreator() { return worldCreator; }

    @Override
    public TestGameConfig getGameConfig() { return null; }
    
    @Override
    public EnvisionGameLauncher createGameLauncher(LauncherSettings settings) {
        return new EnvisionGameLauncher(settings) {
            @Override
            protected void launchGame(LauncherSettings settings) {
                
            }
        };
    }
	
}

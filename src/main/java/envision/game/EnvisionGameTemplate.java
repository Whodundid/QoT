package envision.game;

import envision.engine.events.GameEvent;
import envision.engine.events.IEventListener;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.screens.GameScreen;
import envision.engine.settings.config.EnvisionConfigFile;
import envision.engine.terminal.TerminalCommandHandler;
import envision.launcher.EnvisionGameLauncher;
import envision.launcher.LauncherSettings;

public interface EnvisionGameTemplate extends IEventListener {
	
    //===========
    // Abstracts
    //===========
    
    String getGameName();
    String getGameVersionString();
    
    <T extends EnvisionGameSettings> T getGameSettings();
    <T extends EnvisionConfigFile> T getConfigFile();
    <T extends AbstractWorldCreator> T getWorldCreator();
    
    default <T extends EnvisionGameLauncher> T createGameLauncher(LauncherSettings settings) {
        return null;
    }
    
    //=================
    // Default Methods
    //=================
    
    /** Returns this game's starting screen. */
    default GameScreen getStartScreen() {
        return null;
    }
    
    /** Returns this game's main menu screen. */
    default GameScreen getMainMenuScreen() {
        return null;
    }
    
    /** Returns this game's new game screen. */
    default GameScreen getNewGameScreen() {
        return null;
    }
    
	default void onRenderTick() {}
	default void onGameTick() {}
	
	default void onMouseInput(int action, int mX, int mY, int button, int change) {}
	default void onMousePress(int mX, int mY, int button) {}
	default void onMouseRelease(int mX, int mY, int button) {}
	
	default void onKeyInput(int action, char typedChar, int keyCode) {}
	default void onKeyPress(char typedChar, int keyCode) {}
	default void onKeyRelease(char typedChar, int keyCode) {}
	
	default void onWindowResized() {}
	
	default void onEvent(GameEvent e) {}
	
	default void onTerminalLoad(TerminalCommandHandler handler) {}
	
	default void onPreEngineLoad() {}
	default void onPostEngineLoad() {}
	default void onPreEngineUnload() {}
	default void onPostEngineUnload() {}
	
	default void onPreGameUnload() {}
	default void onPostGameUnload() {}
	
	default void onRegisterInternalTextures(TextureSystem textureSystem) {}
	
}

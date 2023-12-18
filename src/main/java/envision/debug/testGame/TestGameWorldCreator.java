package envision.debug.testGame;

import java.io.File;

import envision.game.AbstractWorldCreator;
import envision.game.world.GameWorld;

public class TestGameWorldCreator extends AbstractWorldCreator {
    
    private TestGame game;
    private TestGameSettings settings;
    
    public TestGameWorldCreator(TestGame gameIn) {
        game = gameIn;
        settings = game.getGameSettings();
    }
    
    @Override
    public GameWorld createGameWorldInstance(String worldName) {
        File f = new File(worldName);
        return new TestGameWorld(f);
    }

    @Override
    public GameWorld createGameWorldInstance(String worldName, int width, int height) {
        return new TestGameWorld(worldName, width, height);
    }
    
}

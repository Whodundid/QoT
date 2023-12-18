package envision.debug.testGame;

import java.io.File;

import envision.game.world.GameWorld;

public class TestGameWorld extends GameWorld {

    protected TestGameWorld(File worldFile) { super(worldFile); }
    
    protected TestGameWorld(String worldName, int width, int height) { super(worldName, width, height); }
    
}

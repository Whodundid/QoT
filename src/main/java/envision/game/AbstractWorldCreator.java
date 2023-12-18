package envision.game;

import envision.game.world.GameWorld;

public abstract class AbstractWorldCreator {
    
    public abstract GameWorld createGameWorldInstance(String worldName);
    public abstract GameWorld createGameWorldInstance(String worldName, int width, int height);
    
}

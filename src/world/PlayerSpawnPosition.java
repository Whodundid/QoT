package world;

import eutil.storage.Box2;

/** A point in the world where the player will be positioned when being loaded.
 *  The coordinates for positioning are based off of the top left corner of the player's collision box. */
public class PlayerSpawnPosition {
	
	/** The world for which this point is corresponding to. */
	GameWorld theWorld;
	int xPos = 0, yPos = 0;
	
	protected PlayerSpawnPosition(GameWorld worldIn) { this(worldIn, 0, 0); }
	public PlayerSpawnPosition(GameWorld worldIn, int xIn, int yIn) {
		theWorld = worldIn;
		xPos = xIn;
		yPos = yIn;
	}
	
	public GameWorld getWorld() { return theWorld; }
	public Box2<Integer, Integer> getPos() { return new Box2(xPos, yPos); }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	
	public PlayerSpawnPosition setPos(int xIn, int yIn ) { xPos = xIn; yPos = yIn; return this; }
	public PlayerSpawnPosition setX(int xIn) { xPos = xIn; return this; }
	public PlayerSpawnPosition setY(int yIn) { yPos = yIn; return this; }
	
}

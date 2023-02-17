package envision.game.world.worldEditor.editorUtil;

import envision.game.objects.GameObject;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.datatypes.boxes.Box2;

/** A point in the world where the player will be positioned when being loaded.
 *  The coordinates for positioning are based off of the top left corner of the player's collision box. */
public class PlayerSpawnPoint extends GameObject {
	
	/** The world for which this point is corresponding to. */
	GameWorld theWorld;
	int xPos = 0, yPos = 0;
	
	public PlayerSpawnPoint() { this(null, -1, -1); }
	public PlayerSpawnPoint(GameWorld worldIn) { this(worldIn, 0, 0); }
	public PlayerSpawnPoint(GameWorld worldIn, int xIn, int yIn) {
		super("Spawn");
		theWorld = worldIn;
		xPos = xIn;
		yPos = yIn;
	}
	
	public GameWorld getWorld() { return theWorld; }
	public Box2<Integer, Integer> getPos() { return new Box2(xPos, yPos); }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	
	public PlayerSpawnPoint setPos(int xIn, int yIn ) { xPos = xIn; yPos = yIn; return this; }
	public PlayerSpawnPoint setX(int xIn) { xPos = xIn; return this; }
	public PlayerSpawnPoint setY(int yIn) { yPos = yIn; return this; }
	
	@Override
	public int getInternalSaveID() {
		return 6;
	}
	
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		
	}
	
}

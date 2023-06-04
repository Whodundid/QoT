package envision.game.world.worldEditor.editorUtil;

import envision.game.GameObject;
import envision.game.world.GameWorld;
import eutil.datatypes.points.Point2i;
import qot.assets.textures.editor.EditorTextures;

/**
 * A point in the world where the player will be positioned when being
 * loaded. The coordinates for positioning are based off of the top left
 * corner of the player's collision box.
 */
public class PlayerSpawnPoint extends GameObject {
	
	/** The world for which this point is corresponding to. */
	GameWorld theWorld;
	int xPos = 0, yPos = 0;
	int layerIndex = 0;
	
	public PlayerSpawnPoint() { this(null, -1, -1); }
	public PlayerSpawnPoint(GameWorld worldIn) { this(worldIn, 0, 0); }
	public PlayerSpawnPoint(GameWorld worldIn, int xIn, int yIn) {
		super("Spawn");
		init(xIn, yIn, 32, 32);
		setTexture(EditorTextures.player_spawn);
		
		theWorld = worldIn;
		xPos = xIn;
		yPos = yIn;
	}
	
	@Override
	public String toString() {
		return layerIndex + "," + xPos + "," + yPos;
	}
	
	public GameWorld getWorld() { return theWorld; }
	public Point2i getPos() { return new Point2i(xPos, yPos); }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	public int getLayerIndex() { return layerIndex; }
	
	public PlayerSpawnPoint setPos(int xIn, int yIn) { xPos = xIn; yPos = yIn; return this; }
	public PlayerSpawnPoint setX(int xIn) { xPos = xIn; return this; }
	public PlayerSpawnPoint setY(int yIn) { yPos = yIn; return this; }
	public PlayerSpawnPoint setLayerIndex(int index) { layerIndex = index; return this; }
	
	@Override
	public int getInternalSaveID() {
		return 6;
	}
	
}

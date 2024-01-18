package envision.game.world.worldEditor.editorUtil;

import envision.engine.assets.EditorTextures;
import envision.engine.registry.types.Sprite;
import envision.game.GameObject;
import envision.game.world.GameWorld;

/**
 * A point in the world where the player will be positioned when being
 * loaded. The coordinates for positioning are based off of the top left
 * corner of the player's collision box.
 */
public class PlayerSpawnPoint extends GameObject {
	
	/** The world for which this point is corresponding to. */
	GameWorld theWorld;
	int layerIndex = 0;
	
	public PlayerSpawnPoint() { this(null, -1, -1); }
	public PlayerSpawnPoint(GameWorld worldIn) { this(worldIn, 0, 0); }
	public PlayerSpawnPoint(GameWorld worldIn, double xIn, double yIn) {
		super("Spawn");
		init((int) xIn, (int) yIn, 32, 32);
		setCollisionBox(0, 0, 0, 0);
		setSprite(new Sprite(EditorTextures.player_spawn));
		
		theWorld = worldIn;
		this.setPixelPos(xIn, yIn);
	}
	
	@Override
	public String toString() {
		return layerIndex + "," + startX + "," + startY;
	}
	
	public GameWorld getWorld() { return theWorld; }
	public int getLayerIndex() { return layerIndex; }
	public PlayerSpawnPoint setLayerIndex(int index) { layerIndex = index; return this; }
	
	@Override
	public int getInternalSaveID() {
		return 6;
	}
	
}

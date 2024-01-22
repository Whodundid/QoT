package envision.game.world.worldTiles;

/**
 * A special internal tile that represents the absence of a tile.
 * 
 * @author Hunter
 */
public class VoidTile extends WorldTile {

    public static final VoidTile instance = new VoidTile();
    
    public VoidTile() { this(0, 0); }
	public VoidTile(int x, int y) {
		tileName = "VOID";
		worldX = x;
		worldY = y;
		setBlocksMovement(true);
	}
	
	@Override
	public int getID() {
	    return -1;
	}
	
	@Override
	public WorldTile copy() {
	    return instance;
		//return copyFields(this, new VoidTile());
	}
	
	public static boolean isVoid(WorldTile tile) {
	    return tile == null || tile instanceof VoidTile;
	}
	
	public static boolean notVoid(WorldTile tile) {
	    return tile != null && !(tile instanceof VoidTile);
	}
	
}

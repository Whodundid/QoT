package envision.game.world.worldTiles;

public class VoidTile extends WorldTile {

	public VoidTile() { this(0, 0); }
	public VoidTile(int x, int y) {
		name = "VOID";
		worldX = x;
		worldY = y;
		
		blocksMovement = true;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override public int getID() { return -1; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new VoidTile());
	}
	
}

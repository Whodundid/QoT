package qot.world_tiles;

import envision.game.world.worldTiles.WorldTile;

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

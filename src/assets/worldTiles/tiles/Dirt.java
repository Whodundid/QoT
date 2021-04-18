package assets.worldTiles.tiles;

import assets.worldTiles.TileIDs;
import assets.worldTiles.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		blocksMovement = false;
	}
	
}

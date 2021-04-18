package assets.worldTiles.tiles;

import assets.worldTiles.TileIDs;
import assets.worldTiles.WorldTile;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		
		setBlocksMovement(true);
	}
	
}

package assets.worldTiles.tiles;

import assets.worldTiles.TileIDs;
import assets.worldTiles.WorldTile;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setBlocksMovement(true);
	}
	
}

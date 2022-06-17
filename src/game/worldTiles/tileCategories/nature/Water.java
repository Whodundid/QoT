package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		
		setBlocksMovement(true);
	}
	
}

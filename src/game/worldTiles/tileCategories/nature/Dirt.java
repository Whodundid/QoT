package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		blocksMovement = false;
	}
	
}

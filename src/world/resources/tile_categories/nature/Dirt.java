package world.resources.tile_categories.nature;

import world.resources.TileIDs;
import world.resources.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		blocksMovement = false;
	}
	
}

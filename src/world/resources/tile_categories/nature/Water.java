package world.resources.tile_categories.nature;

import world.resources.TileIDs;
import world.resources.WorldTile;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		
		setBlocksMovement(true);
	}
	
}

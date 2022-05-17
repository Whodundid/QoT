package world.resources.tile_categories.nature;

import world.resources.TileIDs;
import world.resources.WorldTile;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setBlocksMovement(true);
	}
	
}

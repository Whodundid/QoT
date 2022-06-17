package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		setWall(true);
		wallHeight = 0.1;
	}
	
}

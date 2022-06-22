package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Farm3 extends WorldTile {
	
	public Farm3() {
		super(TileIDs.FARM3);
		blocksMovement = true;
		setWall(true);
		this.wallHeight = 0.20;
	}
	
}

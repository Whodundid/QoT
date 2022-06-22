package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Farm0 extends WorldTile {
	
	public Farm0() {
		super(TileIDs.FARM0);
		blocksMovement = true;
		setWall(true);
		this.wallHeight = 0.20;
	}
	
}

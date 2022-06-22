package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Farm1 extends WorldTile {
	
	public Farm1() {
		super(TileIDs.FARM1);
		blocksMovement = true;
		setWall(true);
		this.wallHeight = 0.20;
	}
	
}

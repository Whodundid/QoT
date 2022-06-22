package game.worldTiles.tileCategories.dungeon;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		
		setWall(true);
		this.wallHeight = 0.05;
	}
	
}
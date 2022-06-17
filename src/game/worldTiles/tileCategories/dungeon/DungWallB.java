package game.worldTiles.tileCategories.dungeon;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class DungWallB extends WorldTile {
	
	public DungWallB() {
		super(TileIDs.DUNG_WALL_B);
		setBlocksMovement(true);
		setWall(true);
	}
	
}
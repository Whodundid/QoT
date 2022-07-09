package game.worldTiles.tileCategories;

import game.worldTiles.TileCategory;
import game.worldTiles.WorldTile;
import game.worldTiles.tileCategories.dungeon.DungFloor;
import game.worldTiles.tileCategories.dungeon.DungOldWallA;
import game.worldTiles.tileCategories.dungeon.DungOldWallB;
import game.worldTiles.tileCategories.dungeon.DungWallA;
import game.worldTiles.tileCategories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

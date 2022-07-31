package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.dungeon.DungFloor;
import world.worldTiles.categories.dungeon.DungOldWallA;
import world.worldTiles.categories.dungeon.DungOldWallB;
import world.worldTiles.categories.dungeon.DungWallA;
import world.worldTiles.categories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

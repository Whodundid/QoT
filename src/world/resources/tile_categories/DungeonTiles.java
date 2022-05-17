package world.resources.tile_categories;

import world.resources.TileCategory;
import world.resources.WorldTile;
import world.resources.tile_categories.dungeon.DungFloor;
import world.resources.tile_categories.dungeon.DungOldWallA;
import world.resources.tile_categories.dungeon.DungOldWallB;
import world.resources.tile_categories.dungeon.DungWallA;
import world.resources.tile_categories.dungeon.DungWallB;

public class DungeonTiles extends TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

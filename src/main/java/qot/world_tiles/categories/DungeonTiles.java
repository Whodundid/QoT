package qot.world_tiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import qot.world_tiles.categories.dungeon.BlueDungTile;
import qot.world_tiles.categories.dungeon.DarkDungFloor;
import qot.world_tiles.categories.dungeon.DungFloor;
import qot.world_tiles.categories.dungeon.DungOldWallA;
import qot.world_tiles.categories.dungeon.DungOldWallB;
import qot.world_tiles.categories.dungeon.DungWallA;
import qot.world_tiles.categories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	public static final WorldTile dungBlueTile = new BlueDungTile();
	public static final WorldTile dungDarkFloor = new DarkDungFloor();
	
}

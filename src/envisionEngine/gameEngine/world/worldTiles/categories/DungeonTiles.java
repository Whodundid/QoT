package envisionEngine.gameEngine.world.worldTiles.categories;

import envisionEngine.gameEngine.world.worldTiles.TileCategory;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldTiles.categories.dungeon.DungFloor;
import envisionEngine.gameEngine.world.worldTiles.categories.dungeon.DungOldWallA;
import envisionEngine.gameEngine.world.worldTiles.categories.dungeon.DungOldWallB;
import envisionEngine.gameEngine.world.worldTiles.categories.dungeon.DungWallA;
import envisionEngine.gameEngine.world.worldTiles.categories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

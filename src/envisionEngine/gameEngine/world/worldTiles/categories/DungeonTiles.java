package envision.gameEngine.world.worldTiles.categories;

import envision.gameEngine.world.worldTiles.TileCategory;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldTiles.categories.dungeon.DungFloor;
import envision.gameEngine.world.worldTiles.categories.dungeon.DungOldWallA;
import envision.gameEngine.world.worldTiles.categories.dungeon.DungOldWallB;
import envision.gameEngine.world.worldTiles.categories.dungeon.DungWallA;
import envision.gameEngine.world.worldTiles.categories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

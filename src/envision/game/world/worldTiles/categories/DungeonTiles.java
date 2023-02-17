package envision.game.world.worldTiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.categories.dungeon.DungFloor;
import envision.game.world.worldTiles.categories.dungeon.DungOldWallA;
import envision.game.world.worldTiles.categories.dungeon.DungOldWallB;
import envision.game.world.worldTiles.categories.dungeon.DungWallA;
import envision.game.world.worldTiles.categories.dungeon.DungWallB;

public class DungeonTiles implements TileCategory {
	
	public DungeonTiles() {}
	
	public static final WorldTile dungWallA = new DungWallA();
	public static final WorldTile dungWallB = new DungWallB();
	public static final WorldTile dungOldWallA = new DungOldWallA();
	public static final WorldTile dungOldWallB = new DungOldWallB();
	public static final WorldTile dungFloor = new DungFloor();
	
}

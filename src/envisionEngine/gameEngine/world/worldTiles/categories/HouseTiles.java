package envisionEngine.gameEngine.world.worldTiles.categories;

import envisionEngine.gameEngine.world.worldTiles.TileCategory;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldTiles.categories.house.Counter;
import envisionEngine.gameEngine.world.worldTiles.categories.house.Counter_Food;
import envisionEngine.gameEngine.world.worldTiles.categories.house.Oven;
import envisionEngine.gameEngine.world.worldTiles.categories.house.Sink;

public class HouseTiles implements TileCategory {
	
	private HouseTiles() {}
	
	public static final WorldTile counter_food = new Counter_Food();
	public static final WorldTile counter = new Counter();
	public static final WorldTile oven = new Oven();
	public static final WorldTile sink = new Sink();
	
}

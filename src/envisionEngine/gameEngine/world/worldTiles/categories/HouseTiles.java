package envision.gameEngine.world.worldTiles.categories;

import envision.gameEngine.world.worldTiles.TileCategory;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldTiles.categories.house.Counter;
import envision.gameEngine.world.worldTiles.categories.house.Counter_Food;
import envision.gameEngine.world.worldTiles.categories.house.Oven;
import envision.gameEngine.world.worldTiles.categories.house.Sink;

public class HouseTiles implements TileCategory {
	
	private HouseTiles() {}
	
	public static final WorldTile counter_food = new Counter_Food();
	public static final WorldTile counter = new Counter();
	public static final WorldTile oven = new Oven();
	public static final WorldTile sink = new Sink();
	
}

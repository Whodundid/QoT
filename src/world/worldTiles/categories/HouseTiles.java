package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.house.Counter;
import world.worldTiles.categories.house.Counter_Food;
import world.worldTiles.categories.house.Oven;
import world.worldTiles.categories.house.Sink;

public class HouseTiles implements TileCategory {
	
	private HouseTiles() {}
	
	public static final WorldTile counter_food = new Counter_Food();
	public static final WorldTile counter = new Counter();
	public static final WorldTile oven = new Oven();
	public static final WorldTile sink = new Sink();
	
}

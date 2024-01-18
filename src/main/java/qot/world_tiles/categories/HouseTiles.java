package qot.world_tiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import qot.world_tiles.categories.house.Counter;
import qot.world_tiles.categories.house.Counter_Food;
import qot.world_tiles.categories.house.Oven;
import qot.world_tiles.categories.house.Roof;
import qot.world_tiles.categories.house.Sink;

public class HouseTiles implements TileCategory {
	
	private HouseTiles() {}
	
	public static final WorldTile counter_food = new Counter_Food();
	public static final WorldTile counter = new Counter();
	public static final WorldTile oven = new Oven();
	public static final WorldTile sink = new Sink();
	public static final WorldTile roof = new Roof();
	
}

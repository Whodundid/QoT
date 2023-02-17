package envision.game.world.worldTiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.categories.house.Counter;
import envision.game.world.worldTiles.categories.house.Counter_Food;
import envision.game.world.worldTiles.categories.house.Oven;
import envision.game.world.worldTiles.categories.house.Sink;

public class HouseTiles implements TileCategory {
	
	private HouseTiles() {}
	
	public static final WorldTile counter_food = new Counter_Food();
	public static final WorldTile counter = new Counter();
	public static final WorldTile oven = new Oven();
	public static final WorldTile sink = new Sink();
	
}

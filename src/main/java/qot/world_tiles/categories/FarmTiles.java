package qot.world_tiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import qot.world_tiles.categories.farm.FarmPlot;

public class FarmTiles implements TileCategory {
	
	private FarmTiles() {}
	
	public static final WorldTile farmPlot = new FarmPlot();
	
}

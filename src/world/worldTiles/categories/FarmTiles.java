package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.farm.FarmPlot;

public class FarmTiles implements TileCategory {
	
	private FarmTiles() {}
	
	public static final WorldTile farmPlot = new FarmPlot();
	
}

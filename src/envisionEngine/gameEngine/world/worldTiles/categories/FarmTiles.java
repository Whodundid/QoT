package envisionEngine.gameEngine.world.worldTiles.categories;

import envisionEngine.gameEngine.world.worldTiles.TileCategory;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldTiles.categories.farm.FarmPlot;

public class FarmTiles implements TileCategory {
	
	private FarmTiles() {}
	
	public static final WorldTile farmPlot = new FarmPlot();
	
}

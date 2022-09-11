package envision.gameEngine.world.worldTiles.categories;

import envision.gameEngine.world.worldTiles.TileCategory;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldTiles.categories.farm.FarmPlot;

public class FarmTiles implements TileCategory {
	
	private FarmTiles() {}
	
	public static final WorldTile farmPlot = new FarmPlot();
	
}

package envision.game.world.worldTiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.categories.farm.FarmPlot;

public class FarmTiles implements TileCategory {
	
	private FarmTiles() {}
	
	public static final WorldTile farmPlot = new FarmPlot();
	
}

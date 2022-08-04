package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.stone.ClayPad;
import world.worldTiles.categories.stone.StonePad;

public class StoneFloorTiles implements TileCategory {
	
	private StoneFloorTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	
}

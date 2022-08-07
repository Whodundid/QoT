package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.stone.ClayPad;
import world.worldTiles.categories.stone.StonePad;
import world.worldTiles.categories.stone.StoneWall;

public class StoneTiles implements TileCategory {
	
	private StoneTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	public static final WorldTile stoneWall = new StoneWall();
	
}

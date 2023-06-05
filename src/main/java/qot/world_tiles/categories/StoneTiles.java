package qot.world_tiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import qot.world_tiles.categories.stone.ClayPad;
import qot.world_tiles.categories.stone.StonePad;
import qot.world_tiles.categories.stone.StonePaver;
import qot.world_tiles.categories.stone.StoneWall;

public class StoneTiles implements TileCategory {
	
	private StoneTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	public static final WorldTile stoneWall = new StoneWall();
	public static final WorldTile stonePaver = new StonePaver();
	
}

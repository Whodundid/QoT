package envision.gameEngine.world.worldTiles.categories;

import envision.gameEngine.world.worldTiles.TileCategory;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldTiles.categories.stone.ClayPad;
import envision.gameEngine.world.worldTiles.categories.stone.StonePad;
import envision.gameEngine.world.worldTiles.categories.stone.StoneWall;

public class StoneTiles implements TileCategory {
	
	private StoneTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	public static final WorldTile stoneWall = new StoneWall();
	
}

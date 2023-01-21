package envisionEngine.gameEngine.world.worldTiles.categories;

import envisionEngine.gameEngine.world.worldTiles.TileCategory;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldTiles.categories.stone.ClayPad;
import envisionEngine.gameEngine.world.worldTiles.categories.stone.StonePad;
import envisionEngine.gameEngine.world.worldTiles.categories.stone.StoneWall;

public class StoneTiles implements TileCategory {
	
	private StoneTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	public static final WorldTile stoneWall = new StoneWall();
	
}

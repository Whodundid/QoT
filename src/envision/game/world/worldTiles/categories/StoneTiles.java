package envision.game.world.worldTiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.categories.stone.ClayPad;
import envision.game.world.worldTiles.categories.stone.StonePad;
import envision.game.world.worldTiles.categories.stone.StonePaver;
import envision.game.world.worldTiles.categories.stone.StoneWall;

public class StoneTiles implements TileCategory {
	
	private StoneTiles() {}
	
	public static final WorldTile clayPad = new ClayPad();
	public static final WorldTile stonePad = new StonePad();
	public static final WorldTile stoneWall = new StoneWall();
	public static final WorldTile stonePaver = new StonePaver();
	
}

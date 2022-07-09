package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import eutil.random.RandomUtil;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setBlocksMovement(true);
		setTexture(WorldTextures.stone);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
	}
	
}

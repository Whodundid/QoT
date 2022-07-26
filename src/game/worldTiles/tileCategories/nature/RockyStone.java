package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import eutil.random.RandomUtil;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class RockyStone extends WorldTile {
	
	public RockyStone() {
		super(TileIDs.ROCKY_STONE);
		setTexture(WorldTextures.rocky_stone);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
	}
	
}

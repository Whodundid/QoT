package world.worldTiles.categories.nature;

import assets.textures.WorldTextures;
import eutil.random.RandomUtil;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class RockyStone extends WorldTile {
	
	public RockyStone() {
		super(TileIDs.ROCKY_STONE);
		setTexture(WorldTextures.rocky_stone);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
	}
	
}

package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.assets.textures.world.nature.rock.RockTextures;

public class RockyStone extends WorldTile {
	
	public RockyStone() {
		super(TileIDs.ROCKY_STONE);
		setTexture(RockTextures.rocky_stone);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
		
		rotation = Rotation.random();
	}
	
}
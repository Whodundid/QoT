package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.assets.textures.world.floors.stone.StoneFloorTextures;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setTexture(StoneFloorTextures.stone_pad);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
		
		rotation = Rotation.random();
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Stone());
	}
	
}

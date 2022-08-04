package world.worldTiles.categories.nature;

import assets.textures.world.floors.stone.StoneFloorTextures;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setTexture(StoneFloorTextures.stone_pad);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
		
		rotation = Rotation.random();
	}
	
}

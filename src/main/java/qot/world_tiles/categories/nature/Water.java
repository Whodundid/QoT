package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.water.WaterTextures;
import qot.world_tiles.TileIDs;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setTexture(WaterTextures.water);
		setWall(true);
		wallHeight = -0.05;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Water());
	}
	
}

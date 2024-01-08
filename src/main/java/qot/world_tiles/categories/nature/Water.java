package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.water.WaterTextures;
import qot.world_tiles.TileIDs;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setSprite(new Sprite(WaterTextures.water));
		setWall(true);
		wallHeight = -0.05;
		setMiniMapColor(0xff0062A8);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Water());
	}
	
}

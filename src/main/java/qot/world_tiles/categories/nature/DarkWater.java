package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.water.WaterTextures;
import qot.world_tiles.TileIDs;

public class DarkWater extends WorldTile {
	
	public DarkWater() {
		super(TileIDs.DARK_WATER);
		setBlocksMovement(true);
		setSprite(new Sprite(WaterTextures.dark_water));
		setWall(true);
		wallHeight = -0.05;
		setMiniMapColor(0xff181838);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DarkWater());
	}
	
}

package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class RedSand extends WorldTile {
	
	public RedSand() {
		super(TileIDs.RED_SAND);
		setSprite(new Sprite(SandTextures.red_sand));
		setWall(true);
		wallHeight = 0.05;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new RedSand());
	}
	
}

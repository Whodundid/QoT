package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DarkGrass extends WorldTile {
	
	public DarkGrass() {
		super(TileIDs.DARK_GRASS);
		setTexture(GrassTextures.dark_grass);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DarkGrass());
	}
	
}

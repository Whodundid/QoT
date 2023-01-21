package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;

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

package world.worldTiles.categories.nature;

import assets.textures.world.nature.grass.GrassTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DarkGrass extends WorldTile {
	
	public DarkGrass() {
		super(TileIDs.DARK_GRASS);
		setTexture(GrassTextures.dark_grass);
	}
	
}

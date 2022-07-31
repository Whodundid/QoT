package world.worldTiles.categories.nature;

import assets.textures.WorldTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DarkGrass extends WorldTile {
	
	public DarkGrass() {
		super(TileIDs.DARK_GRASS);
		setTexture(WorldTextures.dark_grass);
	}
	
}

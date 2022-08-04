package world.worldTiles.categories.nature;

import assets.textures.world.nature.NatureTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class CrackedDirt extends WorldTile {
	
	public CrackedDirt() {
		super(TileIDs.CRACKED_DIRT);
		setTexture(NatureTextures.cracked_dirt);
	}
	
}

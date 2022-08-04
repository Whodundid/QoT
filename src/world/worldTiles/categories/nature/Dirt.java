package world.worldTiles.categories.nature;

import assets.textures.world.nature.NatureTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		setTexture(NatureTextures.dirt);
	}
	
}

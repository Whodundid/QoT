package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		setTexture(WorldTextures.dirt);
	}
	
}

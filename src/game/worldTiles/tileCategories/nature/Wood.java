package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Wood extends WorldTile {
	
	public Wood() {
		super(TileIDs.WOOD);
		setTexture(WorldTextures.wood);
	}
	
}
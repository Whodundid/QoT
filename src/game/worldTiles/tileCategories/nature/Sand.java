package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		setWall(true);
		setTexture(WorldTextures.sand);
		wallHeight = 0.1;
	}
	
}

package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		setTexture(WorldTextures.sand);
		setWall(true);
		wallHeight = 0.025;
	}
	
}

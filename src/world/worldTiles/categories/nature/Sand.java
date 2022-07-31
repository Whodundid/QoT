package world.worldTiles.categories.nature;

import assets.textures.WorldTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		setTexture(WorldTextures.sand);
		setWall(true);
		wallHeight = 0.025;
	}
	
}

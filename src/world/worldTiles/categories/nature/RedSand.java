package world.worldTiles.categories.nature;

import assets.textures.WorldTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class RedSand extends WorldTile {
	
	public RedSand() {
		super(TileIDs.RED_SAND);
		setTexture(WorldTextures.red_sand);
		setWall(true);
		wallHeight = 0.05;
	}
	
}

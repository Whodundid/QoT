package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class RedSand extends WorldTile {
	
	public RedSand() {
		super(TileIDs.RED_SAND);
		setTexture(WorldTextures.red_sand);
		setWall(true);
		wallHeight = 0.05;
	}
	
}

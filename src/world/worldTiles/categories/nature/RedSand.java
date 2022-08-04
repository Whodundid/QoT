package world.worldTiles.categories.nature;

import assets.textures.world.nature.sand.SandTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class RedSand extends WorldTile {
	
	public RedSand() {
		super(TileIDs.RED_SAND);
		setTexture(SandTextures.red_sand);
		setWall(true);
		wallHeight = 0.05;
	}
	
}

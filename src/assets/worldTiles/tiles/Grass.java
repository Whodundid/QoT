package assets.worldTiles.tiles;

import assets.textures.WorldTextures;
import assets.worldTiles.TileIDs;
import assets.worldTiles.WorldTile;

public class Grass extends WorldTile {
	
	public Grass() { this(-1); }
	public Grass(int id) {
		super(TileIDs.GRASS);
		
		numVariants = 4;
		
		if (id < 0) {
			setTexture(WorldTextures.grass.getRandVariant());
		}
		else {
			setTexture(WorldTextures.grass.getChild(id));
		}
		
		blocksMovement = false;
	}
	
}

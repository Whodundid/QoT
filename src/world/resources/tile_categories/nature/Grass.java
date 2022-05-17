package world.resources.tile_categories.nature;

import assets.textures.WorldTextures;
import world.resources.TileIDs;
import world.resources.WorldTile;

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

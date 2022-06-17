package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

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

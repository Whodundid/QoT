package world.worldTiles.categories.nature;

import assets.textures.world.nature.grass.GrassTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class LeafyGrass extends WorldTile {
	
	public LeafyGrass() { this(-1); }
	public LeafyGrass(int id) {
		super(TileIDs.LEAFY_GRASS);
		numVariants = GrassTextures.leafy_grass.getChildren().size();
		
		if (id < 0) {
			setTexture(GrassTextures.leafy_grass.getRandVariant());
		}
		else {
			setTexture(GrassTextures.leafy_grass.getChild(id));
		}
	}
	
}

package world.worldTiles.categories.nature;

import assets.textures.world.nature.grass.GrassTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class LightGrass extends WorldTile {
	
	public LightGrass() { this(-1); }
	public LightGrass(int id) {
		super(TileIDs.LIGHT_GRASS);
		numVariants = GrassTextures.light_grass.getChildren().size();
		
		if (id < 0) {
			setTexture(GrassTextures.light_grass.getRandVariant());
		}
		else {
			setTexture(GrassTextures.light_grass.getChild(id));
		}
	}
	
}

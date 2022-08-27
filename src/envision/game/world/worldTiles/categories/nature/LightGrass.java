package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

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

package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

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
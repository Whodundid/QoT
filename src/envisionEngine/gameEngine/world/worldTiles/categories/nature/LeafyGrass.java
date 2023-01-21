package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;

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
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LeafyGrass());
	}
	
}

package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

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

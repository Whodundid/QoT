package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

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
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LightGrass());
	}
	
}

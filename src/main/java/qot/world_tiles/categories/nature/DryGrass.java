package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DryGrass extends WorldTile {
	
	public DryGrass() { this(-1); }
	public DryGrass(int id) {
		super(TileIDs.DRY_GRASS);
		setSideTexture(GrassTextures.dry_grass);
		numVariants = GrassTextures.dry_grass.getChildren().size();
		
		if (id < 0) {
			setTexture(GrassTextures.dry_grass.getRandVariant());
		}
		else {
			setTexture(GrassTextures.dry_grass.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DryGrass());
	}
	
}

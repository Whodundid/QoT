package world.worldTiles.categories.nature;

import assets.textures.world.nature.grass.GrassTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

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
	
}

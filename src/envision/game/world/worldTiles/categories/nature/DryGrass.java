package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

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

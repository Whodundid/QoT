package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DryPlainsGrass extends WorldTile {
	
	public DryPlainsGrass() {
		super(TileIDs.DRY_PLAINS_GRASS);
		setTexture(GrassTextures.dry_plains_grass);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DryPlainsGrass());
	}
	
}

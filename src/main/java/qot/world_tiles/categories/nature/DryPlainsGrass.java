package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DryPlainsGrass extends WorldTile {
	
	public DryPlainsGrass() {
		super(TileIDs.DRY_PLAINS_GRASS);
		setSprite(new Sprite(GrassTextures.dry_plains_grass));
		setMiniMapColor(0xff825B19);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DryPlainsGrass());
	}
	
}

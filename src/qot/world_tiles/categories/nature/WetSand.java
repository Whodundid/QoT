package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class WetSand extends WorldTile {
	
	public WetSand() {
		super(TileIDs.WET_SAND);
		setTexture(SandTextures.wet_sand);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new WetSand());
	}
	
}

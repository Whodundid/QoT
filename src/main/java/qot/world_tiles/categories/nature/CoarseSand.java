package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class CoarseSand extends WorldTile {
	
	public CoarseSand() { this(-1); }
	public CoarseSand(int id) {
		super(TileIDs.SAND);
		numVariants = 3;
		
		if (id < 0) {
			setTexture(SandTextures.coarse_sand.getRandVariant());
		}
		else {
			setTexture(SandTextures.coarse_sand.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new CoarseSand());
	}
	
}
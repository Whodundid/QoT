package world.worldTiles.categories.nature;

import assets.textures.world.nature.sand.SandTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

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
	
}

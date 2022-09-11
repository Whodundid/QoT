package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.sand.SandTextures;

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

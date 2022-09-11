package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

public class Grass extends WorldTile {
	
	public Grass() { this(-1); }
	public Grass(int id) {
		super(TileIDs.GRASS);
		
		numVariants = 3;
		
		if (id < 0) {
			setTexture(GrassTextures.grass.getRandVariant());
		}
		else {
			setTexture(GrassTextures.grass.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Grass());
	}
	
}

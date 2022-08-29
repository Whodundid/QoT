package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
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

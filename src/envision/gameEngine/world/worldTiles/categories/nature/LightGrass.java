package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

public class LightGrass extends WorldTile {
	
	public LightGrass() { this(-1); }
	public LightGrass(int id) {
		super(TileIDs.LIGHT_GRASS);
		numVariants = GrassTextures.light_grass.getChildren().size();
		
		if (id < 0) {
			setTexture(GrassTextures.light_grass.getRandVariant());
		}
		else {
			setTexture(GrassTextures.light_grass.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LightGrass());
	}
	
}

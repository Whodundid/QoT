package qot.world_tiles.categories.nature;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class Grass extends WorldTile {
	
	public Grass() { this(-1); }
	public Grass(int id) {
		super(TileIDs.GRASS);
		
		numVariants = 3;
		
		if (id < 0) {
		    setSprite(new Sprite(GrassTextures.grass.getRandVariant()));
		}
		else {
		    setSprite(new Sprite(GrassTextures.grass.getChild(id)));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Grass());
	}
	
}

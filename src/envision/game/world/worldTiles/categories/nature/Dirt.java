package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.NatureTextures;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		setTexture(NatureTextures.dirt);
	}
	
}

package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.NatureTextures;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		setTexture(NatureTextures.dirt);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Dirt());
	}
	
}

package envision.game.world.worldTiles.categories.house;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

public class Sink extends WorldTile {
	
	public Sink() {
		super(TileIDs.SINK);
		setTexture(HouseTextures.sink);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Sink());
	}
	
}
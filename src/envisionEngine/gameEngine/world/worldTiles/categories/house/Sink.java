package envisionEngine.gameEngine.world.worldTiles.categories.house;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;

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
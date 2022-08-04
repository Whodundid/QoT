package world.worldTiles.categories.house;

import assets.textures.doodads.house.HouseTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Sink extends WorldTile {
	
	public Sink() {
		super(TileIDs.SINK);
		setTexture(HouseTextures.sink);
		blocksMovement = true;
	}
	
}
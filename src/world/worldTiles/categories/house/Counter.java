package world.worldTiles.categories.house;

import assets.textures.doodads.house.HouseTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Counter extends WorldTile {
	
	public Counter() {
		super(TileIDs.COUNTER);
		setTexture(HouseTextures.counter);
		blocksMovement = true;
	}
	
}
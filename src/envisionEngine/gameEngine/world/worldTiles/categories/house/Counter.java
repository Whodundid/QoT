package envisionEngine.gameEngine.world.worldTiles.categories.house;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;

public class Counter extends WorldTile {
	
	public Counter() {
		super(TileIDs.COUNTER);
		setTexture(HouseTextures.counter);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Counter());
	}
	
}
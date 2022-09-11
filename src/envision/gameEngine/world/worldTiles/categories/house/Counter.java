package envision.gameEngine.world.worldTiles.categories.house;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

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
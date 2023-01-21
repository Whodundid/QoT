package envisionEngine.gameEngine.world.worldTiles.categories.house;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;

public class Oven extends WorldTile {
	
	public Oven() {
		super(TileIDs.OVEN);
		setTexture(HouseTextures.oven);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Oven());
	}
	
}

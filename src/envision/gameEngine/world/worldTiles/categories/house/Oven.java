package envision.gameEngine.world.worldTiles.categories.house;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

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

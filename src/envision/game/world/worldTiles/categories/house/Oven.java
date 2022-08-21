package envision.game.world.worldTiles.categories.house;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

public class Oven extends WorldTile {
	
	public Oven() {
		super(TileIDs.OVEN);
		setTexture(HouseTextures.oven);
		blocksMovement = true;
	}
	
}

package envision.gameEngine.world.worldTiles.categories.house;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
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
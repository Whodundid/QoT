package qot.world_tiles.categories.house;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Sink extends WorldTile {
	
	public Sink() {
		super(TileIDs.SINK);
		setSprite(new Sprite(HouseTextures.sink));
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Sink());
	}
	
}
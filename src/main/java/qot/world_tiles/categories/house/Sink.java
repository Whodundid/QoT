package qot.world_tiles.categories.house;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Sink extends WorldTile {
	
	public Sink() {
		super(TileIDs.SINK);
		setSprite(new Sprite(HouseTextures.sink));
		blocksMovement = true;
		setMiniMapColor(0xff6B7585);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Sink());
	}
	
}
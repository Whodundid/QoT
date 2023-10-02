package qot.world_tiles.categories.house;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Oven extends WorldTile {
	
	public Oven() {
		super(TileIDs.OVEN);
		setSprite(new Sprite(HouseTextures.oven));
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Oven());
	}
	
}

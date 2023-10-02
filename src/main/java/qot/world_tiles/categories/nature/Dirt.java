package qot.world_tiles.categories.nature;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(TileIDs.DIRT);
		setSprite(new Sprite(NatureTextures.dirt));
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Dirt());
	}
	
}

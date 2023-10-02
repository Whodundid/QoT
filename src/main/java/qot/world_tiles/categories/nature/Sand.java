package qot.world_tiles.categories.nature;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		//setTexture(SandTextures.sand);
		setWall(true);
		wallHeight = 0.025;
		
		setSprite(new Sprite(SandTextures.sand));
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Sand());
	}
	
}

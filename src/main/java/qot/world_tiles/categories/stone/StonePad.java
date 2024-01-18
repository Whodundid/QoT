package qot.world_tiles.categories.stone;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class StonePad extends WorldTile {
	
	public StonePad() {
		super(TileIDs.STONE_PAD);
		setSprite(new Sprite(StoneFloorTextures.stone_pad));
		setWall(true);
		wallHeight = 0.1f;
		randomizeRotation = true;
		
		randomizeValues();
		setMiniMapColor(0xff808080);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StonePad());
	}
	
}

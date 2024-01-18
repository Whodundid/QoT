package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setSprite(new Sprite(StoneFloorTextures.stone_pad));
		setBlocksMovement(true);
		setWall(true);
		minWallHeight = 0.25f;
		maxWallHeight = 0.75f;
		randomizeRotation = true;
		randomizeWallHeight = true;
		
		randomizeValues();
		setMiniMapColor(0xff707070);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Stone());
	}
	
}

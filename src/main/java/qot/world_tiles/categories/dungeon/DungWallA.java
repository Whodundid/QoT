package qot.world_tiles.categories.dungeon;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.world_tiles.TileIDs;

public class DungWallA extends WorldTile {
	
	public DungWallA() {
		super(TileIDs.DUNG_WALL_A);
		setBlocksMovement(true);
		setWall(true);
		setSprite(new Sprite(DungeonWallTextures.dung_wall_a));
		setMiniMapColor(0xff161616);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungWallA());
	}
	
}
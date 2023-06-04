package qot.world_tiles.categories.dungeon;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.world_tiles.TileIDs;

public class DungWallA extends WorldTile {
	
	public DungWallA() {
		super(TileIDs.DUNG_WALL_A);
		setBlocksMovement(true);
		setWall(true);
		setTexture(DungeonWallTextures.dung_wall_a);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungWallA());
	}
	
}
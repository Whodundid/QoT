package world.worldTiles.categories.dungeon;

import assets.textures.world.walls.dungeon.DungeonWallTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungWallB extends WorldTile {
	
	public DungWallB() {
		super(TileIDs.DUNG_WALL_B);
		setBlocksMovement(true);
		setWall(true);
		setTexture(DungeonWallTextures.dung_wall_m_a);
	}
	
}
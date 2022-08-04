package world.worldTiles.categories.dungeon;

import assets.textures.world.walls.dungeon.DungeonWallTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungWallA extends WorldTile {
	
	public DungWallA() {
		super(TileIDs.DUNG_WALL_A);
		setBlocksMovement(true);
		setWall(true);
		setTexture(DungeonWallTextures.dung_wall_a);
	}
	
}
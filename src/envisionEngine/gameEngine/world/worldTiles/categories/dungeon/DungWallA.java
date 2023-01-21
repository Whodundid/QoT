package envisionEngine.gameEngine.world.worldTiles.categories.dungeon;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;

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
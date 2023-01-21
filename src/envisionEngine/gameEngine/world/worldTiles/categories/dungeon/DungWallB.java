package envisionEngine.gameEngine.world.worldTiles.categories.dungeon;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;

public class DungWallB extends WorldTile {
	
	public DungWallB() {
		super(TileIDs.DUNG_WALL_B);
		setBlocksMovement(true);
		setWall(true);
		setTexture(DungeonWallTextures.dung_wall_m_a);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungWallB());
	}
	
}
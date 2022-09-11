package envision.gameEngine.world.worldTiles.categories.dungeon;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.dungeon.DungeonWallTextures;

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
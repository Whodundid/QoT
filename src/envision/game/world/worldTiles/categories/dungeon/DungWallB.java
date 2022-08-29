package envision.game.world.worldTiles.categories.dungeon;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
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
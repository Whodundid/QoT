package qot.world_tiles.categories.dungeon;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.world_tiles.TileIDs;

public class DungOldWallB extends WorldTile {
	
	public DungOldWallB() {
		super(TileIDs.DUNG_FLOOR_Bb);
		setTexture(DungeonWallTextures.dung_wall_m_b);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungOldWallB());
	}
	
}
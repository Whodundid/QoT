package envisionEngine.gameEngine.world.worldTiles.categories.dungeon;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;

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
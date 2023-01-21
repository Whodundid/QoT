package envision.gameEngine.world.worldTiles.categories.dungeon;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.dungeon.DungeonWallTextures;

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
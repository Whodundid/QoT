package envisionEngine.gameEngine.world.worldTiles.categories.dungeon;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setTexture(DungeonWallTextures.dung_wall_b);
		setWall(true);
		this.wallHeight = 0.05;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungOldWallA());
	}
	
}
package world.worldTiles.categories.dungeon;

import assets.textures.world.walls.dungeon.DungeonWallTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setTexture(DungeonWallTextures.dung_wall_b);
		setWall(true);
		this.wallHeight = 0.05;
	}
	
}
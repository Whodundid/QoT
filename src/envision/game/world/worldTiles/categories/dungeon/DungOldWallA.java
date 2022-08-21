package envision.game.world.worldTiles.categories.dungeon;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.dungeon.DungeonWallTextures;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setTexture(DungeonWallTextures.dung_wall_b);
		setWall(true);
		this.wallHeight = 0.05;
	}
	
}
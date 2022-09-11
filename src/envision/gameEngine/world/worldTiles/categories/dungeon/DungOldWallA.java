package envision.gameEngine.world.worldTiles.categories.dungeon;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.dungeon.DungeonWallTextures;

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
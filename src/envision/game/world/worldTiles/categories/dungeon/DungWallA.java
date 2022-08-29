package envision.game.world.worldTiles.categories.dungeon;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.dungeon.DungeonWallTextures;

public class DungWallA extends WorldTile {
	
	public DungWallA() {
		super(TileIDs.DUNG_WALL_A);
		setBlocksMovement(true);
		setWall(true);
		setTexture(DungeonWallTextures.dung_wall_a);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungWallA());
	}
	
}
package envision.game.world.dungeonBuilder;

import envision.game.world.util.EntitySpawn;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.categories.DungeonTiles;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;
import game.entities.EntityList;

public class DungeonRoomSquare extends DungeonPiece {
	
	public DungeonRoomSquare(int w, int h, boolean spawn) {
		super(w, h, spawn);
	}
	
	@Override
	public void build() {
		//build the walls first
		for (int i = 0; i < w; i++) { tiles[0][i] = wall(); } //top
		for (int i = 0; i < h; i++) { tiles[i][0] = wall(); } //left
		for (int i = 0; i < h; i++) { tiles[i][w - 1] = wall(); } //right
		for (int i = 0; i < w; i++) { tiles[h - 1][i] = wall(); } //bot
		
		//now the floor
		for (int i = 1; i < w - 1; i++) {
			for (int j = 1; j < h - 1; j++) {
				WorldTile t = DungeonTiles.dungFloor;
				tiles[i][j] = t;
			}
		}
		
		//now open areas
		for (Direction d : open) {
			switch (d) {
			case N: for (int i = (w/2-2); i < (w/2-2) + 5; i++) { tiles[i][0] = DungeonTiles.dungFloor; } break;
			case W: for (int i = (h/2-2); i < (h/2-2) + 5; i++) { tiles[0][i] = DungeonTiles.dungFloor; } break;
			case S: for (int i = (h/2-2); i < (h/2-2) + 5; i++) { tiles[i][w - 1] = DungeonTiles.dungFloor; } break;
			case E: for (int i = (w/2-2); i < (w/2-2) + 5; i++) { tiles[h - 1][i] = DungeonTiles.dungFloor; } break;
			default: break;
			}
		}
		
		int posAx = (w - 3);
		int posAy = (h - 3);
		int posBx = 3;
		int posBy = 3;
		
		//entity spawns
		if (ERandomUtil.roll(1, 0, 1)) addEntity(new EntitySpawn(posAx, posAy, EntityList.randomType().ID));
		if (ERandomUtil.roll(1, 0, 1)) addEntity(new EntitySpawn(posBx, posBy, EntityList.randomType().ID));
	}
	
	public static DungeonRoomSquare createRandom(boolean spawn) {
		//int w = randInt(10, 15);
		//int h = randInt(10, 15);
		return new DungeonRoomSquare(15, 15, spawn);
	}
	
	private WorldTile wall() {
		return (randInt(0, 1) == 1) ? DungeonTiles.dungWallA : DungeonTiles.dungWallB;
	}
	
}

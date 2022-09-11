package envision.gameEngine.world.worldTiles.categories.stone;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.walls.stone.StoneWallTextures;

public class StoneWall extends WorldTile {
	
	public StoneWall() { this(-1); }
	public StoneWall(int id) {
		super(TileIDs.STONE_WALL);
		setWall(true);
		wallHeight = 0.75;
		blocksMovement = true;
		//rotation = Rotation.random();
		//drawFlipped = RandomUtil.randomBool();
		numVariants = StoneWallTextures.stone_wall.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneWallTextures.stone_wall.getRandVariant());
		}
		else {
			setTexture(StoneWallTextures.stone_wall.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StoneWall());
	}
	
}

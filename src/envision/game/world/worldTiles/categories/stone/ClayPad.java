package envision.game.world.worldTiles.categories.stone;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.RandomUtil;
import game.assets.textures.world.floors.stone.StoneFloorTextures;

public class ClayPad extends WorldTile {
	
	public ClayPad() { this(-1); }
	public ClayPad(int id) {
		super(TileIDs.CLAY_PAD);
		setWall(true);
		wallHeight = 0.1;
		//rotation = Rotation.random();
		drawFlipped = RandomUtil.randomBool();
		numVariants = StoneFloorTextures.clay_pad.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneFloorTextures.clay_pad.getRandVariant());
		}
		else {
			setTexture(StoneFloorTextures.clay_pad.getChild(id));
		}
	}
	
}
package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.effects.animations.AnimationHandler;
import envision.game.effects.animations.AnimationSet;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.nature.water.WaterTextures;
import qot.world_tiles.TileIDs;

public class BlueWater extends WorldTile {
	
    private AnimationSet flow;
    
	public BlueWater() {
		super(TileIDs.BLUE_WATER);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = -0.05f;
		spriteStartIndex = 0;
		spriteEndIndex = 4;
		spriteAnimationInterval = 60;
		isAnimated = true;
		
		Sprite sprite = WaterTextures.blue_water_sheet.getSprite(0);
		setSprite(sprite);
		
		animationHandler = new AnimationHandler(this);
		flow = animationHandler.createAnimationSet("flow");
		flow.setUpdateInterval(spriteAnimationInterval);
		final int len = WaterTextures.blue_water_sheet.getNumberOfSprites();
		int startFrame = ERandomUtil.getRoll(0, len - 1);
		for (int i = startFrame; i < (len + startFrame); i++) {
		    int index = i;
		    if (index >= len) index -= len;
		    flow.addFrame(WaterTextures.blue_water_sheet.getSprite(index));
		}
		
		animationHandler.playIfNotAlreadyPlaying("flow");
		
		setMiniMapColor(0xff066378);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new BlueWater());
	}
	
}

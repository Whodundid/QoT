package qot.doodads;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.entities.Doodad;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.trees.TreeTextures;
import qot.entities.EntityList;

public class PineTree extends Doodad {

	public PineTree() { this(0, 0); }
	public PineTree(int posX, int posY) {
		super("pine");
		init(posX, posY, 200, 200);
		sprite = new Sprite(TreeTextures.tree_pine_0);
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		double sx = (facing == Rotation.RIGHT) ? 15 : 10;
		double ex = (facing == Rotation.RIGHT) ? 10 : 15;
		
		setCollisionBox(midX - sx, endY - 15, midX + ex, endY);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.PINE_TREE.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		/*
		double distToPlayer = world.getDistance(this, QoT.thePlayer);
		
		//check if distance to player is less than 200 pixels
		if (distToPlayer <= 200) {
			Direction dirToPlayer = world.getDirectionTo(this, QoT.thePlayer);
			//headText = (int) distToPlayer + " : " + dirToPlayer;
			
			EDimension testDim;
			EDimension pDims;
			
			{
				double cSX = startX + collisionBox.startX;
				double cSY = startY + collisionBox.startY;
				double cEX = endX - (width - collisionBox.endX);
				double cEY = endY - (height - collisionBox.endY);
				
				testDim = new EDimension(cSX, cSY, cEX, cEY);
			}
			
			{
				Entity e = QoT.thePlayer;
				double cSX = e.startX + e.collisionBox.startX;
				double cSY = e.startY + e.collisionBox.startY;
				double cEX = e.endX - (e.width - e.collisionBox.endX);
				double cEY = e.endY - (e.height - e.collisionBox.endY);
				
				pDims = new EDimension(cSX, cSY, cEX, cEY);
			}
			
			///headText = "" + testDim.contains(pDims);
			if (testDim.contains(pDims)) {
				//QoT.pause();
				//QoT.pauseWorldRender();
				//QoT.displayScreen(new CombatScreen(QoT.thePlayer, this), QoT.currentScreen);
			}
			move(dirToPlayer);
		}
		else {
			boolean shouldMove = RandomUtil.roll(10, 0, 10);
			//headText = "";
			
			if (shouldMove) {
				Direction dir = RandomUtil.randomDir();
				move(dir);
			}
		}
		*/
	}
	
}

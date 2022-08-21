package game.doodads;

import envision.game.entity.Entity;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.assets.textures.doodads.trees.TreeTextures;

public class PineTree extends Entity {

	public PineTree() { this(0, 0); }
	public PineTree(int posX, int posY) {
		super("pine");
		init(posX, posY, 256, 256);
		sprite = TreeTextures.tree_pine_0;
		this.facing = (RandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		setCollisionBox(startX + 26, endY - 25, endX - 26, endY);
	}
	
	@Override
	public int getObjectID() {
		return 5;
	}
	
	@Override
	public void onLivingUpdate() {
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

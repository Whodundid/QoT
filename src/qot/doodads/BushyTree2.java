package qot.doodads;

import envision.game.objects.entities.Entity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.trees.TreeTextures;
import qot.entities.EntityList;

public class BushyTree2 extends Entity {

	public BushyTree2() { this(0, 0); }
	public BushyTree2(int posX, int posY) {
		super("bushytree2");
		init(posX, posY, 160, 160);
		tex = TreeTextures.bushy_tree_2;
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		double sx = (facing == Rotation.RIGHT) ? 15 : 5;
		double ex = (facing == Rotation.RIGHT) ? 5 : 15;
		setCollisionBox(midX - sx, endY - 15, midX + ex, endY);
		
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.BUSHY_TREE_2.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}

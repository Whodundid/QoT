package game.entities.neutral;

import assets.textures.EntityTextures;
import game.shops.Shopkeeper;

public class WhodundidsBrother extends Shopkeeper {

	public WhodundidsBrother() { this(0, 0); }
	public WhodundidsBrother(int posX, int posY) {
		super("Whodundid's Brother");
		init(posX, posY, 64, 64);
		
		setTexture(EntityTextures.whodundidsbrother);
		setBaseMeleeDamage(2);
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
	}

	@Override
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getObjectID() {
		return 6;
	}
	
}

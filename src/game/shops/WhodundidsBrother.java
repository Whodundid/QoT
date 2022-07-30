package game.shops;

import assets.textures.EntityTextures;

public class WhodundidsBrother extends Shopkeeper {

	public WhodundidsBrother() {
		super("Whodundid's Brother");
		setTexture(EntityTextures.whodundidsbrother);
	}

	@Override
	public int getObjectID() {
		return 6;
	}
	
}

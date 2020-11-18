package shop;

import gameTextures.EntityTextures;
import main.Game;

public class WhodundidsBrother extends Shopkeeper {
	
	public WhodundidsBrother() {
		this(0, 0);
	}
	public WhodundidsBrother(int posX, int posY) {
		//String nameIn, int levelIn, double maxHealthIn, double healthIn, double maxManaIn, double manaIn, double damageIn, double goldIn
		super("Whodundid's Brother", 928272625, 928272625, 22222222, 12345678, 222, 350);
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.whodundidsbrother);
		super.drawObject(mXIn, mYIn);
	}
	
}

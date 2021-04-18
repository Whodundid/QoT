package assets.entities.neutral;

import assets.textures.EntityTextures;
import gameSystems.shopSystem.Shopkeeper;

public class WhodundidsBrother extends Shopkeeper {
	
	public WhodundidsBrother() {
		this(0, 0);
	}
	
	public WhodundidsBrother(int posX, int posY) {
		//String nameIn, int levelIn, double maxHealthIn, double healthIn, double maxManaIn, double manaIn, double damageIn, double goldIn
		super("Whodundid's Brother", 928272625, 928272625, 22222222, 12345678, 222, 350);
		init(posX, posY, 150, 150);
		sprite = EntityTextures.whodundidsbrother;
	}
	
}

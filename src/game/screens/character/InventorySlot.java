package game.screens.character;

import envision.game.items.Item;
import envision.renderEngine.textureSystem.GameTexture;
import envision.windowLib.windowTypes.WindowObject;

public class InventorySlot extends WindowObject {
	
	Item theItem;
	GameTexture itemTexture;
	
	public InventorySlot(Item itemIn, double xIn, double yIn, double size) {
		theItem = itemIn;
		itemTexture = theItem.getTexture();
		
		setDimensions(xIn, yIn, size, size);
	}
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
	}
	
}

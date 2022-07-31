package game.screens.character;

import engine.renderEngine.textureSystem.GameTexture;
import engine.windowLib.windowTypes.WindowObject;
import game.items.Item;

public class InventorySlot extends WindowObject {
	
	Item theItem;
	GameTexture itemTexture;
	
	public InventorySlot(Item itemIn, double xIn, double yIn, double size) {
		theItem = itemIn;
		itemTexture = theItem.getTexture();
		
		setDimensions(xIn, yIn, size, size);
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
	}
	
}

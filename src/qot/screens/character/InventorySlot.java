package qot.screens.character;

import envisionEngine.gameEngine.gameObjects.items.Item;
import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.windowLib.windowTypes.WindowObject;

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

package game.screens.testingStuff;

import engine.inputHandlers.Keyboard;
import engine.screenEngine.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;

public class TestScreen extends GameScreen {
	
	WindowButton b1;
	WindowTextField textField;
	
	TestInventory inventory;
	private double inventoryX, inventoryY;
	
	//---------------------------
	
	@Override
	public void initChildren() {
		b1 = new WindowButton(this, startX + 15, startY + 15, 50, 35, "T");
		textField = new WindowTextField(this, startX + 200, startY + 200, 150, 30);
		
		IActionObject.setActionReceiver(this, b1, textField);
		
		addChild(b1);
		addChild(textField);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		this.drawRect(EColors.black);
		this.drawRect(EColors.dgray, 10);
		
		if (inventory != null) {
			inventoryX = inventory.startX;
			inventoryY = inventory.startY;
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_T) {
			openInventory();
		}
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == b1) openInventory();
		if (object == textField) checkText();
	}
	
	private void openInventory() {
		//if the inventory window exists and is not closed, close the window
		if (inventory != null && !inventory.isClosed()) {
			inventory.close();
		}
		else {
			//display new inventory screen
			displayWindow(inventory = new TestInventory());
		}
	}
	
	private void checkText() {
		if (textField.getText().equals("yes")) {
			close();
			closeScreen();
		}
	}
	
}

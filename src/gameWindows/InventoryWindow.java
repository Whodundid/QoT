package gameWindows;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.WindowParent;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.player.Player;

public class InventoryWindow extends WindowParent {
	
	Player thePlayer;
	WindowButton use, drop, info;
	
	public InventoryWindow(Player in) {
		super();
		thePlayer = in;
	}
	
	@Override
	public void initWindow() {
		setDimensions(300, 200);
		setObjectName("Inventory");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}

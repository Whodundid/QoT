package gameWindows;

import assets.entities.player.Player;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;

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

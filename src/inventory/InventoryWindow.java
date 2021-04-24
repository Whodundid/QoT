package inventory;

import assets.entities.player.Player;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IActionObject;

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

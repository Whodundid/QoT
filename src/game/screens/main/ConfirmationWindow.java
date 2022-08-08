package game.screens.main;

import assets.textures.window.WindowTextures;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import main.QoT;

public class ConfirmationWindow extends WindowParent {
	private WindowButton<?> confirm, deny;
	
	public ConfirmationWindow(int x, int y) {
		init(QoT.getCurrentScreen(), x, y, 300, 200);
		setMaxDims(400, 200);
		setMinDims(200, 100);
		setMinimizable(false);
		setResizeable(true);
	}
	
	@Override
	public void initChildren() {
		defaultHeader();
		
		confirm = new WindowButton(this, startX + 0, startY + 0, 50, 50);
		deny = new WindowButton(this, startX + 100, startY + 50, 50, 50);
		
		confirm.setTextures(WindowTextures.file_up, WindowTextures.file_up_sel);
		deny.setTextures(WindowTextures.file_pic, WindowTextures.file_txt);
		
		// Makes it so confirm and deny send an action to this confirmation window
		IActionObject.setActionReceiver(this, confirm, deny);
		
		addChild(confirm, deny);
	}
	
	@Override
	public void drawObject(int xPos, int yPos) {
		this.drawDefaultBackground();
		super.drawObject(xPos, yPos);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == confirm) {
			QoT.loadWorld(null); // Unload current world
			QoT.displayScreen(new MainMenuScreen()); // Display main menu
		}
		
		if (object == deny) close();
	}
}

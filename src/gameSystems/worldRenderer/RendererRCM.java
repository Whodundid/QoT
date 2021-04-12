package gameSystems.worldRenderer;

import envisionEngine.eWindow.windowObjects.windows.RightClickMenu;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import storageUtil.EArrayList;

//Author: Hunter Bragg

public class RendererRCM extends RightClickMenu {
	
	@Override
	public void initWindow() {
		addOption("New Window");
		addOption("Open EMC Settings");
		addOption("Close All Objects");
		
		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Options...");
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getStoredObject()) {
			case "New Terminal": openTerminal(); break;
			case "New Window": openGui(); break;
			case "Close All Objects": clearScreen(); break;
			}
		}
	}
	
	private void openTerminal() {
		//Game.displayWindow(new ETerminal(), CenterType.cursor);
	}
	
	private void openGui() {
		//Game.displayWindow(new EMCGuiSelectionList(), CenterType.cursor);
	}
	
	private void clearScreen() {
		EArrayList<IWindowObject> objs = getTopParent().getCombinedObjects();
		//TaskBar bar = ren.getTaskBar();
		
		for (IWindowObject o : objs) {
			//if (bar != null) {
			//	if (o == bar || o.isChild(bar)) { continue; }
			//}
			
			if (o.isCloseable()) { o.close(); }
		}
		//EnhancedMC.displayWindow(null);
	}
	
}

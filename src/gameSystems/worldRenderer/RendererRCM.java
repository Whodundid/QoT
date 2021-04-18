package gameSystems.worldRenderer;

import storageUtil.EArrayList;
import windowLib.windowObjects.windows.RightClickMenu;
import windowLib.windowTypes.interfaces.IActionObject;
import windowLib.windowTypes.interfaces.IWindowObject;

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
			switch ((String) getGenericObject()) {
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

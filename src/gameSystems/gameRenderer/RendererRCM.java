package gameSystems.gameRenderer;

import eWindow.windowObjects.windows.RightClickMenu;
import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class RendererRCM extends RightClickMenu {
	
	@Override
	public void initWindow() {
		//if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT)) { addOption("New Chat Window"); }
		//if (EnhancedMC.getEMCApp().enableTerminal.get()) { addOption("New Terminal", EMCResources.terminalButton); }
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
			switch ((String) getSelectedObject()) {
			case "New Chat Window": openChatWindow(); break;
			case "New Terminal": openTerminal(); break;
			case "New Window": openGui(); break;
			case "Open EMC Settings": openSettings(); break;
			case "Close All Objects": clearScreen(); break;
			}
		}
	}
	
	private void openSettings() {
		//Game.displayWindow(new SettingsWindowMain(), CenterType.cursor);
	}
	
	private void openChatWindow() {
		/*
		if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT)) {
			RegisteredApps.getApp(AppType.ENHANCEDCHAT).sendArgs("EnhancedChat: add window cursor");
		}
		*/
	}
	
	private void openTerminal() {
		//Game.displayWindow(new ETerminal(), CenterType.cursor);
	}
	
	private void openGui() {
		//Game.displayWindow(new EMCGuiSelectionList(), CenterType.cursor);
	}
	
	private void clearScreen() {
		GameRenderer ren = GameRenderer.getInstance();
		EArrayList<IWindowObject> objs = EArrayList.combineLists(ren.getObjects(), ren.getAddingObjects());
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

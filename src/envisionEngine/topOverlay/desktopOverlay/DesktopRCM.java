package envisionEngine.topOverlay.desktopOverlay;

import envisionEngine.inputHandlers.Mouse;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import envisionEngine.windowLib.windowObjects.utilityObjects.RightClickMenu;
import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import envisionEngine.windowLib.windowUtil.ObjectPosition;
import qot.QoT;
import qot.assets.textures.window.WindowTextures;

public class DesktopRCM extends RightClickMenu {
	
	public static final String sel_open = "Open";
	public static final String sel_nfile = "New File";
	public static final String sel_nshort = "New Shortcut";
	public static final String sel_term = "Open Terminal";
	
	public DesktopRCM() {
		setTitle("Desktop");
		addOption(sel_open, WindowTextures.file_folder);
		addOption(sel_nfile, WindowTextures.file_txt);
		addOption(sel_nshort, WindowTextures.forward);
		addOption(sel_term, WindowTextures.terminal);
		
		setActionReceiver(this);
	}
	
	public static void checkOpen(int action, int button) {
		var top = QoT.getTopRenderer();
		if (action == 1 && button == 1) {				
			var highest = top.getHighestZObjectUnderMouse();
			if (highest == null || !highest.isVisible()) {
				var old = top.getWindowInstance(DesktopRCM.class);
				if (old != null) old.close();
				top.displayWindow(new DesktopRCM(), ObjectPosition.CURSOR_CORNER);
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (args.length == 1 && args[0] instanceof String selection) {
			switch (selection) {
			case sel_open -> handle_open();
			case sel_nfile -> handle_newFile();
			case sel_nshort -> handle_shortcut();
			case sel_term -> handle_terminal();
			}
		}
	}
	
	public void handle_open() {
		QoT.getTopRenderer().displayWindow(new FileExplorerWindow(), ObjectPosition.CURSOR_CENTER);
	}
	
	public void handle_newFile() {
		var shortcut = new OverlayShortcut(Mouse.getMx(), Mouse.getMy());
		shortcut.setIcon(WindowTextures.file_txt);
		shortcut.setDescription("New Text File");
		QoT.getTopRenderer().addObject(shortcut);
	}
	
	public void handle_shortcut() {
		var shortcut = new OverlayShortcut(Mouse.getMx(), Mouse.getMy());
		shortcut.setIcon(WindowTextures.file);
		QoT.getTopRenderer().addObject(shortcut);
	}
	
	public void handle_terminal() {
		QoT.getTopRenderer().displayWindow(new ETerminal(), ObjectPosition.CURSOR_CENTER);
	}
	
}

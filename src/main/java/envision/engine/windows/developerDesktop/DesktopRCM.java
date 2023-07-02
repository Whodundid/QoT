package envision.engine.windows.developerDesktop;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowUtil.ObjectPosition;
import qot.assets.textures.window.WindowTextures;

public class DesktopRCM extends RightClickMenu {
	
	public DesktopRCM() {
		setTitle("Desktop");
		addOption("File Explorer", WindowTextures.file_folder, () -> handle_openFileExplorer());
		addOption("New File", WindowTextures.file_txt, () -> handle_newFile());
		addOption("New Shortcut", WindowTextures.forward, () -> handle_shortcut());
		addOption("Open Terminal", WindowTextures.terminal, () -> handle_terminal());
	}
	
	public static void checkOpen(int action, int button) {
		var top = Envision.getTopScreen();
		if (action == 1 && button == 1) {				
			var highest = top.getHighestZObjectUnderMouse();
			if (highest == null || !highest.isVisible()) {
				var old = top.getWindowInstance(DesktopRCM.class);
				if (old != null) old.close();
				top.displayWindow(new DesktopRCM(), ObjectPosition.CURSOR_CORNER);
			}
		}
	}
	
	public void handle_openFileExplorer() {
		Envision.getTopScreen().displayWindow(new FileExplorerWindow(), ObjectPosition.CURSOR_CENTER);
	}
	
	public void handle_newFile() {
//		var shortcut = new DesktopShortcut(Mouse.getMx(), Mouse.getMy());
//		shortcut.setIcon(WindowTextures.file_txt);
//		shortcut.setDescription("New Text File");
//		Envision.getTopScreen().addObject(shortcut);
	}
	
	public void handle_shortcut() {
//		var shortcut = new DesktopShortcut(Mouse.getMx(), Mouse.getMy());
//		shortcut.setIcon(WindowTextures.file);
//		Envision.getTopScreen().addObject(shortcut);
	}
	
	public void handle_terminal() {
		Envision.getTopScreen().displayWindow(new ETerminalWindow(), ObjectPosition.CURSOR_CENTER);
	}
	
}

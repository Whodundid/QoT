package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.screens.ScreenLevel;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		var w = new FileExplorerWindow(null);
		
		Envision.displayWindow(ScreenLevel.TOP, w);
	}

}

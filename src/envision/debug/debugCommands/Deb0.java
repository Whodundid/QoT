package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.screens.ScreenLevel;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		var w = new FileExplorerWindow(null);
		
		QoT.displayWindow(ScreenLevel.TOP, w);
	}

}

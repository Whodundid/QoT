package engine.debug.debugCommands;

import engine.screenEngine.ScreenLevel;
import engine.terminal.window.ETerminal;
import engine.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import main.QoT;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminal termIn, Object... args) {
		var w = new FileExplorerWindow(null);
		
		QoT.displayWindow(ScreenLevel.TOP, w);
	}

}

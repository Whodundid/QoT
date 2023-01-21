package envisionEngine.debug.debugCommands;

import envisionEngine.gameEngine.gameSystems.screens.ScreenLevel;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminal termIn, Object... args) {
		var w = new FileExplorerWindow(null);
		
		QoT.displayWindow(ScreenLevel.TOP, w);
	}

}

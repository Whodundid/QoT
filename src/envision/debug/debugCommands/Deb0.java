package envision.debug.debugCommands;

import envision.gameEngine.gameSystems.screens.ScreenLevel;
import envision.terminal.window.ETerminal;
import envision.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import game.QoT;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminal termIn, Object... args) {
		var w = new FileExplorerWindow(null);
		
		QoT.displayWindow(ScreenLevel.TOP, w);
	}

}

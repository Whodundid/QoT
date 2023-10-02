package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.HuntDistanceMap;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.developerDesktop.config.DesktopConfigParser;

@SuppressWarnings("unused")
public class Deb10 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		DeveloperDesktop.buildDesktopFromConfig();
	}

}
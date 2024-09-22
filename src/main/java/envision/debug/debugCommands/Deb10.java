package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.HuntDistanceMap;
import envision.engine.kernel.developerDesktop.DeveloperDesktop;
import envision.engine.kernel.developerDesktop.config.DesktopConfigParser;
import envision.engine.kernel.terminal.window.ETerminalWindow;

@SuppressWarnings("unused")
public class Deb10 extends DebugCommand {

    @Override
    public void run(ETerminalWindow termIn, Object... args) {
        DeveloperDesktop.buildDesktopFromConfig();
    }

}
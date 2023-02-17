package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.windows.windowBuilder.WindowBuilderScreen;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb6 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		QoT.displayScreen(new WindowBuilderScreen());
	}

}
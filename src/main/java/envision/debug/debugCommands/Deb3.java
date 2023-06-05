package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.TestWindow;
import envision.engine.terminal.window.ETerminalWindow;

@SuppressWarnings("unused")
public class Deb3 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		Envision.getTopScreen().displayWindow(new TestWindow(100, 100, 700, 500));
	}

}

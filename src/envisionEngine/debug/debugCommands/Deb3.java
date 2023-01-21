package envisionEngine.debug.debugCommands;

import envisionEngine.debug.testStuff.TestWindow;
import envisionEngine.terminal.window.ETerminal;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb3 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new TestWindow(700, 500));
	}

}

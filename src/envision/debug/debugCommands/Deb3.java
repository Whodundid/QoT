package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import envision.debug.testStuff.TestWindow;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb3 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new TestWindow(700, 500));
	}

}

package engine.debug.debugCommands;

import engine.debug.testStuff.TestWindow;
import engine.terminal.window.ETerminal;
import main.QoT;

@SuppressWarnings("unused")
public class Deb3 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new TestWindow(700, 500));
	}

}

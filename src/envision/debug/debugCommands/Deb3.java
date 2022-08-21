package envision.debug.debugCommands;

import envision.debug.testStuff.TestWindow;
import envision.terminal.window.ETerminal;
import game.QoT;

@SuppressWarnings("unused")
public class Deb3 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new TestWindow(700, 500));
	}

}

package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import qot.screens.testingStuff.TestScreen;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		Envision.displayScreen(new TestScreen());
	}

}
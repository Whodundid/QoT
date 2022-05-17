package engine.debug.debugCommands;

import engine.QoT;
import engine.screens.MainMenuScreen;
import engine.screens.TestScreen;
import engine.terminal.window.ETerminal;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen(), new MainMenuScreen());
	}

}
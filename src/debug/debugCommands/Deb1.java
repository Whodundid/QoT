package debug.debugCommands;

import assets.screens.types.MainMenuScreen;
import assets.screens.types.TestScreen;
import debug.terminal.window.ETerminal;
import main.QoT;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen(), new MainMenuScreen());
	}

}
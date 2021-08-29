package debug.debugCommands;

import debug.terminal.window.ETerminal;
import gameScreens.MainMenuScreen;
import gameScreens.TestScreen;
import main.QoT;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen(), new MainMenuScreen());
	}

}
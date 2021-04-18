package debug.debugCommands;

import gameScreens.MainMenuScreen;
import gameScreens.TestScreen;
import main.Game;
import terminal.window.ETerminal;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		Game.displayScreen(new TestScreen(), new MainMenuScreen());
	}

}
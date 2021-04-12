package debug.debugCommands;

import envisionEngine.terminal.window.ETerminal;
import gameScreens.MainMenuScreen;
import gameScreens.TestScreen;
import main.Game;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		Game.displayScreen(new TestScreen(), new MainMenuScreen());
	}

}
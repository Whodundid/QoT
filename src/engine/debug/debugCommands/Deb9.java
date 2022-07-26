package engine.debug.debugCommands;

import engine.screens.testingStuff.TestScreen;
import engine.terminal.window.ETerminal;
import game.EntityLevel;
import main.QoT;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen());
	}

}
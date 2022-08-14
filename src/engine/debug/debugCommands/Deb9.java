package engine.debug.debugCommands;

import engine.terminal.window.ETerminal;
import game.entities.EntityLevel;
import game.screens.testingStuff.TestScreen;
import main.QoT;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminal<?> termIn, Object... args) {
		QoT.displayScreen(new TestScreen());
	}

}
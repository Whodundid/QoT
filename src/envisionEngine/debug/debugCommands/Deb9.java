package envisionEngine.debug.debugCommands;

import envisionEngine.gameEngine.gameObjects.entity.EntityLevel;
import envisionEngine.terminal.window.ETerminal;
import qot.QoT;
import qot.screens.testingStuff.TestScreen;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen());
	}

}
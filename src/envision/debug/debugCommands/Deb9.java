package envision.debug.debugCommands;

import envision.game.entity.EntityLevel;
import envision.terminal.window.ETerminal;
import game.QoT;
import game.screens.testingStuff.TestScreen;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new TestScreen());
	}

}
package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import envision.game.objects.entities.EntityLevel;
import qot.QoT;
import qot.screens.testingStuff.TestScreen;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		QoT.displayScreen(new TestScreen());
	}

}
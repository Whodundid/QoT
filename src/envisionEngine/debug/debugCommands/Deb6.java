package envisionEngine.debug.debugCommands;

import envisionEngine.terminal.window.ETerminal;
import envisionEngine.windowBuilder.WindowBuilderScreen;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb6 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.displayScreen(new WindowBuilderScreen());
	}

}
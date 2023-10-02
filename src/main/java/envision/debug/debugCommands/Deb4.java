package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.TestScreen;
import envision.engine.terminal.window.ETerminalWindow;

@SuppressWarnings("unused")
public class Deb4 extends DebugCommand {
	
	@Override
	public void run(ETerminalWindow termIn, Object... args) {
	    Envision.displayScreen(new TestScreen());
	}
	
}

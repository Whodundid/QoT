package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.HuntDistanceMap;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.entities.util.EntityLevel;

@SuppressWarnings("unused")
public class Deb11 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		for (int i = 0; i < 50; i++) {
		    termIn.writeln((long) i, ": ", EntityLevel.getXPNeededForNextLevel(i), " : ", EntityLevel.getTotalXPNeeded(i));
		}
	}

}
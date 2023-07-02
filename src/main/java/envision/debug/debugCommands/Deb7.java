package envision.debug.debugCommands;

import envision.engine.terminal.window.ETerminalWindow;
import envision.game.entities.util.EntityLevel;
import envision.game.util.IDrawable;
import envision.game.util.InsertionSort;
import eutil.datatypes.EArrayList;

@SuppressWarnings("unused")
public class Deb7 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		for (int i = 0; i <= 50; i++) {
			System.out.println(i + " : " + EntityLevel.calculateBaseDamage(i));
		}
	}

}
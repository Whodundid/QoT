package envisionEngine.debug.debugCommands;

import envisionEngine.gameEngine.gameObjects.entity.EntityLevel;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.util.IDrawable;
import envisionEngine.util.InsertionSort;
import eutil.datatypes.EArrayList;

@SuppressWarnings("unused")
public class Deb7 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		for (int i = 0; i <= 50; i++) {
			System.out.println(i + " : " + EntityLevel.calculateBaseDamage(i));
		}
	}

}
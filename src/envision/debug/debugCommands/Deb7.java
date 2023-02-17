package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import envision.game.objects.IDrawable;
import envision.game.objects.InsertionSort;
import envision.game.objects.entities.EntityLevel;
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
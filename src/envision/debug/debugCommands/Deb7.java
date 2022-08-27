package envision.debug.debugCommands;

import envision.terminal.window.ETerminal;
import envision.util.IDrawable;
import envision.util.InsertionSort;
import eutil.datatypes.EArrayList;

@SuppressWarnings("unused")
public class Deb7 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		
		EArrayList<IDrawable> objects = new EArrayList<>();
		
		objects.add(() -> 10);
		objects.add(() -> 45);
		objects.add(() -> 23);
		objects.add(() -> 32);
		objects.add(() -> 3);
		objects.add(() -> 56);
		objects.add(() -> 44);
		
		InsertionSort.sort(objects);
		
	}

}
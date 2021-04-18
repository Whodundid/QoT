package debug.debugCommands;

import main.Game;
import terminal.window.ETerminal;
import windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		Game.getTopRenderer().displayWindow(new ColorPickerSimple(Game.getTopRenderer()));
	}

}
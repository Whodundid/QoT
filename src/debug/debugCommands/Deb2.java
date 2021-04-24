package debug.debugCommands;

import debug.terminal.window.ETerminal;
import main.QoT;
import windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new ColorPickerSimple(QoT.getTopRenderer()));
	}

}
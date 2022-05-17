package engine.debug.debugCommands;

import engine.QoT;
import engine.terminal.window.ETerminal;
import engine.windowLib.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getTopRenderer().displayWindow(new ColorPickerSimple(QoT.getTopRenderer()));
	}

}
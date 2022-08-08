package engine.debug.debugCommands;

import engine.terminal.window.ETerminal;
import game.screens.main.ConfirmationWindow;
import main.QoT;

@SuppressWarnings("unused")
public class Deb7 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT.getCurrentScreen().displayWindow(new ConfirmationWindow(300, 300));
		
	}

}
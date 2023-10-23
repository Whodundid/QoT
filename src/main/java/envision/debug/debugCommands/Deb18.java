package envision.debug.debugCommands;

import java.util.PriorityQueue;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.dialog.DialogueCutscene;
import qot.entities.enemies.Whodundid;
import qot.entities.Cell;

@SuppressWarnings("unused")
public class Deb18 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		DialogueCutscene currentCutscene = new DialogueCutscene(Envision.thePlayer, new Whodundid());
		currentCutscene.start();
	}

}
package envision.debug.debugCommands;

import envision.Envision;
import envision.debug.testStuff.HuntDistanceMap;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.dialog.DialogueCutscene;
import qot.entities.enemies.Whodundid;

@SuppressWarnings("unused")
public class Deb18 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		DialogueCutscene currentCutscene = new DialogueCutscene(Envision.thePlayer, new Whodundid());
		currentCutscene.start();
	}

}
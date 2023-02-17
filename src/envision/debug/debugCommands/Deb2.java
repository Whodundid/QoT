package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import qot.QoT;
import qot.entities.enemies.Goblin;
import qot.entities.enemies.Thyrah;
import qot.entities.enemies.TrollBoar;
import qot.entities.enemies.Whodundid;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.combat.BattleScreen;
import qot.screens.gameplay.combat.Party;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		Party A = new Party();
		Party B = new Party();
		
		A.setSlot(0, new Goblin());
		A.setSlot(1, new Whodundid());
		A.setSlot(2, new Thyrah());
		A.setSlot(3, new TrollBoar());
		
		B.setSlot(0, new Goblin());
		B.setSlot(1, new Whodundid());
		B.setSlot(2, new Thyrah());
		B.setSlot(3, new TrollBoar());
		
		QoT.displayScreen(new BattleScreen(A, B));
	}

}
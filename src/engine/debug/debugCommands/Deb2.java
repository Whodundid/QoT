package engine.debug.debugCommands;

import engine.screens.combat.BattleScreen;
import engine.screens.combat.Party;
import engine.terminal.window.ETerminal;
import game.entities.Goblin;
import game.entities.Player;
import game.entities.Thyrah;
import game.entities.TrollBoar;
import game.entities.Whodundid;
import main.QoT;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
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
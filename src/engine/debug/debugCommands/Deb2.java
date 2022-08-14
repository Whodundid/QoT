package engine.debug.debugCommands;

import engine.terminal.window.ETerminal;
import game.entities.enemies.Goblin;
import game.entities.enemies.Thyrah;
import game.entities.enemies.TrollBoar;
import game.entities.enemies.Whodundid;
import game.entities.player.Player;
import game.screens.gameplay.combat.BattleScreen;
import game.screens.gameplay.combat.Party;
import main.QoT;

@SuppressWarnings("unused")
public class Deb2 extends DebugCommand {

	@Override
	public void run(ETerminal<?> termIn, Object... args) {
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
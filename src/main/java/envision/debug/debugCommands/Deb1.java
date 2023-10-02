package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import qot.entities.enemies.Goblin;
import qot.entities.player.QoT_Player;
import qot.screens.character.CharacterScreen;
import qot.screens.main.MainMenuScreen;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		QoT_Player p = new QoT_Player();
		System.out.println(p.getSprite().getWidth());
		CharacterScreen charScreen = new CharacterScreen(new Goblin());
		
		Envision.displayScreen(charScreen, new MainMenuScreen());
	}

}
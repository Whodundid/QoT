package envision.debug.debugCommands;

import envision.terminal.window.ETerminal;
import game.QoT;
import game.entities.player.QoT_Player;
import game.screens.character.CharacterScreen;
import game.screens.main.MainMenuScreen;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		QoT_Player p = new QoT_Player();
		System.out.println(p.getTexture().getWidth());
		CharacterScreen charScreen = new CharacterScreen(p);
		
		QoT.displayScreen(charScreen, new MainMenuScreen());
	}

}
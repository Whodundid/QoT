package engine.debug.debugCommands;

import engine.terminal.window.ETerminal;
import game.entities.player.Player;
import game.screens.character.CharacterScreen;
import game.screens.main.MainMenuScreen;
import main.QoT;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminal<?> termIn, Object... args) {
		Player p = new Player();
		System.out.println(p.getTexture().getWidth());
		CharacterScreen charScreen = new CharacterScreen(p);
		
		QoT.displayScreen(charScreen, new MainMenuScreen());
	}

}
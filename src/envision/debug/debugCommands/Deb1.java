package envision.debug.debugCommands;

import envision.debug.terminal.window.ETerminalWindow;
import qot.QoT;
import qot.entities.player.QoT_Player;
import qot.screens.character.CharacterScreen;
import qot.screens.main.MainMenuScreen;

@SuppressWarnings("unused")
public class Deb1 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		QoT_Player p = new QoT_Player();
		System.out.println(p.getTexture().getWidth());
		CharacterScreen charScreen = new CharacterScreen(p);
		
		QoT.displayScreen(charScreen, new MainMenuScreen());
	}

}
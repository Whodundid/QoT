package envisionEngine.debug.debugCommands;

import envisionEngine.renderEngine.fontRenderer.FontRenderer;
import envisionEngine.terminal.window.ETerminal;
import qot.QoT;

@SuppressWarnings("unused")
public class Deb5 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		if (args.length > 0) {
			if (args[0] instanceof String s) {
				if (s.equals("8")) FontRenderer.setCurrentFont(FontRenderer.font8);
				if (s.equals("n")) FontRenderer.setCurrentFont(FontRenderer.newFont);
				if (s.equals("d")) FontRenderer.setCurrentFont(FontRenderer.defaultFont);
				if (s.equals("c")) FontRenderer.setCurrentFont(FontRenderer.courier);
			}
		}
		else {
			FontRenderer.setCurrentFont(FontRenderer.newFont);
		}
	}

}

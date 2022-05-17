package engine.debug.debugCommands;

import engine.QoT;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.terminal.window.ETerminal;

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

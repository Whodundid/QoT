package envision.debug.debugCommands;

import envision.engine.kernel.terminal.window.ETerminalWindow;
import envision.engine.rendering.fontRenderer.FontRenderer;

@SuppressWarnings("unused")
public class Deb5 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		if (args.length > 0) {
			if (args[0] instanceof String s) {
				if (s.equals("8")) FontRenderer.setCurrentFont(FontRenderer.font8);
				if (s.equals("n")) FontRenderer.setCurrentFont(FontRenderer.newFont);
				if (s.equals("d")) FontRenderer.setCurrentFont(FontRenderer.defaultFont);
				if (s.equals("c")) FontRenderer.setCurrentFont(FontRenderer.courier);
				if (s.equals("s")) FontRenderer.setCurrentFont(FontRenderer.smooth);
			}
		}
		else {
			FontRenderer.setCurrentFont(FontRenderer.newFont);
		}
	}

}

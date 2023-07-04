package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.screens.ScreenLevel;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.GameOptionsWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_RenderSettings extends TerminalCommand {
	
	public CMD_RenderSettings() {
		setCategory("Engine");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "renderer"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("rset"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides a means for interfacing with the game's rendering engine"; }
	@Override public String getUsage() { return "ex: rset'"; }

	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		Envision.displayWindow(ScreenLevel.TOP, new GameOptionsWindow());
	}
	
}

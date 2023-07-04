package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_NoClip extends TerminalCommand {
	
	public CMD_NoClip() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "noclip"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("nc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Allows player no-clipping"; }
	@Override public String getUsage() { return "ex: nc"; }
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (Envision.thePlayer != null) {
			Envision.thePlayer.setNoClipAllowed(!Envision.thePlayer.isNoClipping());
			termIn.writeln(((Envision.thePlayer.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
		}
		else {
			termIn.error("There isn't a player!");
		}
	}
	
}

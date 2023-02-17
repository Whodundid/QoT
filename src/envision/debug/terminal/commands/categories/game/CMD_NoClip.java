package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

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
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (QoT.thePlayer != null) {
			QoT.thePlayer.setNoClipAllowed(!QoT.thePlayer.isNoClipping());
			termIn.writeln(((QoT.thePlayer.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
		}
		else {
			termIn.error("There isn't a player!");
		}
	}
	
}

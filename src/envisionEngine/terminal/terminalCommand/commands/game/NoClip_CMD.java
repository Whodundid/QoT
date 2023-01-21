package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class NoClip_CMD extends TerminalCommand {
	
	public NoClip_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "noclip"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("nc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Allows player no-clipping"; }
	@Override public String getUsage() { return "ex: nc"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (QoT.thePlayer != null) {
			QoT.thePlayer.setNoClipAllowed(!QoT.thePlayer.isNoClipping());
			termIn.writeln(((QoT.thePlayer.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
		}
		else {
			termIn.error("There isn't a player!");
		}
	}
	
}

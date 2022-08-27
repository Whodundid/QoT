package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

public class NoClip_CMD extends TerminalCommand {
	
	public NoClip_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "noclip"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("nc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Allows player no-clipping"; }
	@Override public String getUsage() { return "ex: nc"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (QoT.thePlayer != null) {
			QoT.thePlayer.setNoClipAllowed(!QoT.thePlayer.isNoClipping());
			termIn.writeln(((QoT.thePlayer.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
		}
		else {
			termIn.error("There isn't a player!");
		}
	}
	
}

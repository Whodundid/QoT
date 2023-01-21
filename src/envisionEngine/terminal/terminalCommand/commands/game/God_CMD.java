package envisionEngine.terminal.terminalCommand.commands.game;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class God_CMD extends TerminalCommand {
	
	public God_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "god"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("godmode"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles god mode on the player."; }
	@Override public String getUsage() { return "ex: god"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.errorUsage(ERROR_TOO_MANY, getUsage());
			return;
		}
		
		if (QoT.thePlayer == null) {
			termIn.error("There is no player!");
			return;
		}
		
		//toggle
		QoT.thePlayer.setInvincible(!QoT.thePlayer.isInvincible());
		termIn.writeln("Godmode ", (QoT.thePlayer.isInvincible()) ? "enabled!" : "disabled!");
	}
	
}

package envision.terminal.terminalCommand.commands.game;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

public class God_CMD extends TerminalCommand {
	
	public God_CMD() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "god"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("godmode"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles god mode on the player."; }
	@Override public String getUsage() { return "ex: god"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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

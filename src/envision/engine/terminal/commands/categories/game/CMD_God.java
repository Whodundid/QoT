package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_God extends TerminalCommand {
	
	public CMD_God() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "god"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("godmode"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles god mode on the player."; }
	@Override public String getUsage() { return "ex: god"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.errorUsage(ERROR_TOO_MANY, getUsage());
			return;
		}
		
		if (Envision.thePlayer == null) {
			termIn.error("There is no player!");
			return;
		}
		
		//toggle
		Envision.thePlayer.setInvincible(!Envision.thePlayer.isInvincible());
		termIn.writeln("Godmode ", (Envision.thePlayer.isInvincible()) ? "enabled!" : "disabled!");
	}
	
}

package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.datatypes.util.EList;

public class CMD_God extends TerminalCommand {
	
	public CMD_God() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "god"; }
	@Override public EList<String> getAliases() { return EList.of("godmode"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles god mode on the player."; }
	@Override public String getUsage() { return "ex: god"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
		
		if (Envision.thePlayer == null) {
			error("There is no player!");
			return;
		}
		
		//toggle
		Envision.thePlayer.setInvincible(!Envision.thePlayer.isInvincible());
		writeln("Godmode ", (Envision.thePlayer.isInvincible()) ? "enabled!" : "disabled!");
	}
	
}

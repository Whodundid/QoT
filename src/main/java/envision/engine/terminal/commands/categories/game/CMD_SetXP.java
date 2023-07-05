package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CMD_SetXP extends TerminalCommand {
	
	public CMD_SetXP() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "setxp"; }
	@Override public EList<String> getAliases() { return EList.of("xp"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the total xp amount for the player."; }
	@Override public String getUsage() { return "ex: xp 1000"; }
	
	@Override
	public void runCommand() {
	    expectAtLeast(1);

	    if (Envision.thePlayer == null) {
			error("There is no player!");
			return;
		}
		
		var p = Envision.thePlayer;
		
		//toggle
		p.experience = ENumUtil.parseLong(arg(0), p.getExperience());
		writeln("Set player xp to: '", p.getExperience(), "' !");
	}
	
}

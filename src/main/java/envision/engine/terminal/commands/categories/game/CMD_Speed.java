package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.math.ENumUtil;

public class CMD_Speed extends TerminalCommand {
	
	public CMD_Speed() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "speed"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Sets the speed of the player."; }
	@Override public String getUsage() { return "ex: speed 10"; }
	
	@Override
	public void runCommand() {
		if (Envision.thePlayer == null) {
			error("There is no player!");
			return;
		}
		
		expectAtLeast(1);
		
		//toggle
		Envision.thePlayer.speed = ENumUtil.parseDouble(arg(0), 32.0 * 4.5);
		writeln("Set player speed to: '", Envision.thePlayer.speed, "' !");
	}
	
}

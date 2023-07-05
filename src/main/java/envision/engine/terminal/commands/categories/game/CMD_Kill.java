package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_Kill extends TerminalCommand {
	
	public CMD_Kill() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "kill"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("k"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Kills an Entity Based on Entity ID"; }
	@Override public String getUsage() { return "ex: kill 0"; }
	
	@Override
	public void runCommand() {
	    expectExactly(1);
	    
		if (Envision.theWorld == null) {
			error("Current World is Null");
			return;
		}
		
		var entities = Envision.theWorld.getEntitiesInWorld();
		
		int id = Integer.parseInt(firstArg());
		for (var e : entities) {
			if (e.getWorldID() == id) {
				Envision.theWorld.removeEntity(e);
				writeln("Killed entity! " + id, EColors.lgreen);
				break;
			}
		}
	}
	
}

package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;

public class CMD_Stats extends TerminalCommand {
	
	public CMD_Stats() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "stats"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays an entity's stats based on Entity ID"; }
	@Override public String getUsage() { return "ex: stats 5 (where 5 is the id of the entity)"; }
	
	@Override
	public Object runCommand() {
	    expectExactly(1);
	    
		if (Envision.theWorld == null) {
			error("Current World is Null");
			return null;
		}
		
		var entities = Envision.theWorld.getEntitiesInWorld();
		
		int id = parseInt(firstArg());
		for (var e : entities) {
			if (e.getWorldID() == id) {
				//Envision.theWorld.removeEntity(e);
				//writeln("Killed entity! " + id, EColors.lgreen);
				break;
			}
		}
		
		return null;
	}
	
}

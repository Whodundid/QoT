package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_ListEntities extends TerminalCommand {
	
	public CMD_ListEntities() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "listentities"; }
	@Override public EList<String> getAliases() { return EList.of("le", "entities"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays all Entities in the World"; }
	@Override public String getUsage() { return "ex: le"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
	    
		if (Envision.theWorld == null) {
			error("Current World is Null");
			return null;
		}
		
		var entities = Envision.theWorld.getEntitiesInWorld();
		entities.sort((a, b) -> Long.compare(a.getWorldID(), b.getWorldID()));
		
		writeln("Listing all Entities in world", EColors.orange);
		for (var e : entities) {
			writeln("  ", EColors.lgreen, e.getName(), EColors.white, " : ", EColors.lgray, e.getWorldID());
		}
		
		return null;
	}
	
}

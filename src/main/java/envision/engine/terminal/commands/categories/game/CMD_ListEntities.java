package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_ListEntities extends TerminalCommand {
	
	public CMD_ListEntities() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "listentities"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("le", "entities"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays all Entities in the World"; }
	@Override public String getUsage() { return "ex: le"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (Envision.theWorld == null) {
			termIn.error("Current World is Null");
			return;
		}
		
		var entities = Envision.theWorld.getEntitiesInWorld();
		entities.sort((a, b) -> Long.compare(a.getWorldID(), b.getWorldID()));
		
		termIn.writeln("Listing all Entities in world", EColors.orange);
		for (var e : entities) {
			termIn.writeln("  " + EColors.lgreen + e.getName() + EColors.white + " : " + EColors.lgray + e.getWorldID());
		}
	}
	
}

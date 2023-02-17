package envision.debug.terminal.commands.categories.game;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

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
		if (QoT.theWorld == null) {
			termIn.error("Current World is Null");
			return;
		}
		
		var entities = QoT.theWorld.getEntitiesInWorld();
		entities.sort((a, b) -> Long.compare(a.getObjectID(), b.getObjectID()));
		
		termIn.writeln("Listing all Entities in world", EColors.orange);
		for (var e : entities) {
			termIn.writeln("  " + EColors.lgreen + e.getName() + EColors.white + " : " + EColors.lgray + e.getObjectID());
		}
	}
	
}

package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.IListableCommand;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import main.QoT;

public class ListEntities_CMD extends TerminalCommand implements IListableCommand {
	
	public ListEntities_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "listentities"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("le", "entities"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays all Entities in the World"; }
	@Override public String getUsage() { return "ex: le"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (QoT.theWorld == null) {
			termIn.error("Current World is Null");
			return;
		}
		list(termIn, args, runVisually);
	}

	@Override
	public void list(ETerminal termIn, EList<String> args, boolean runVisually) {
		var entities = QoT.theWorld.getEntitiesInWorld();
		entities.sort((a, b) -> Integer.compare(a.getEntityID(), b.getEntityID()));
		
		termIn.writeln("Listing all Entities in world", EColors.orange);
		for (var e : entities) {
			termIn.writeln("  " + EColors.lgreen + e.getName() + EColors.white + " : " + EColors.lgray + e.getEntityID());
		}
	}
	
}
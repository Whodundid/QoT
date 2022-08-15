package engine.terminal.terminalCommand.commands.game;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;

public class Kill_CMD extends TerminalCommand {
	
	public Kill_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "kill"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("k"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Kills an Entity Based on Entity ID"; }
	@Override public String getUsage() { return "ex: kill 0"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (QoT.theWorld == null) {
			termIn.error("Current World is Null");
			return;
		}
		
		if (args.length() != 1) {
			termIn.error("This command only accepts one argument");
			termIn.info(getUsage());
			return;
		}
		
		var entities = QoT.theWorld.getEntitiesInWorld();
		
		int id = Integer.parseInt(args.get(0));
		for (var e : entities) {
			if (e.getEntityID() == id) {
				QoT.theWorld.removeEntity(e);
				termIn.writeln("Killed entity! " + id, EColors.lgreen);
				break;
			}
		}
	}
	
}

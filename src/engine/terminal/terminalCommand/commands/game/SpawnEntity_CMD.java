package engine.terminal.terminalCommand.commands.game;

import engine.QoT;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

public class SpawnEntity_CMD extends TerminalCommand {
	
	public SpawnEntity_CMD() {
		super(CommandType.NORMAL);
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "spawn_entity"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("spawn"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Spawns in an entity near the player"; }
	@Override public String getUsage() { return "ex: spawn goblin"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.error("Entity type must be specified!");
			termIn.info(getUsage());
		}
		else if (args.size() > 1) {
			termIn.error("Too many arguments!");
			termIn.info(getUsage());
		}
		else if (QoT.theWorld == null) {
			termIn.error("Error! There is no world loaded for which to spawn entities in!");
		}
		else {
			//QoT.theWorld.addEntity(null);
		}
	}
	
}

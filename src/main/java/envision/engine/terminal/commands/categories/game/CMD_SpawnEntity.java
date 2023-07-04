package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_SpawnEntity extends TerminalCommand {
	
	public CMD_SpawnEntity() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "spawn_entity"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("spawn"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Spawns in an entity in the world"; }
	@Override public String getUsage() { return "ex: spawn 'ent_name' x y"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		
	}
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.error("Entity type must be specified!");
			termIn.info(getUsage());
		}
		else if (args.size() != 3) {
			termIn.error("Too many arguments!");
			termIn.info(getUsage());
		}
		else if (Envision.theWorld == null) {
			termIn.error("Error! There is no world loaded for which to spawn entities in!");
		}
		else {
			//QoT.theWorld.addEntity(null);
		}
	}
	
}

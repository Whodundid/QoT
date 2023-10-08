package envision.engine.terminal.commands.categories.game;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public class CMD_SpawnEntity extends TerminalCommand {
	
	public CMD_SpawnEntity() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "spawn_entity"; }
	@Override public EList<String> getAliases() { return EList.of("spawn"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Spawns in an entity in the world"; }
	@Override public String getUsage() { return "ex: spawn 'ent_name' x y"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		
	}
	
	@Override
	public Object runCommand() {
	    expectExactly(3);
	    
	    return null;
	}
	
}

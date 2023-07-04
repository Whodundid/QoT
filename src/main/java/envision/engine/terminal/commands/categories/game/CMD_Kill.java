package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
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
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (Envision.theWorld == null) {
			termIn.error("Current World is Null");
			return;
		}
		
		if (args.length() != 1) {
			termIn.error("This command only accepts one argument");
			termIn.info(getUsage());
			return;
		}
		
		var entities = Envision.theWorld.getEntitiesInWorld();
		
		int id = Integer.parseInt(args.get(0));
		for (var e : entities) {
			if (e.getWorldID() == id) {
				Envision.theWorld.removeEntity(e);
				termIn.writeln("Killed entity! " + id, EColors.lgreen);
				break;
			}
		}
	}
	
}

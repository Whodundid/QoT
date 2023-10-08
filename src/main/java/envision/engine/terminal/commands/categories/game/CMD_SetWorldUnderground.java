package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_SetWorldUnderground extends TerminalCommand {
	
	public CMD_SetWorldUnderground() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "underground"; }
	@Override public EList<String> getAliases() { return EList.of("under"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Modifies whether or not the current world is underground or not."; }
	@Override public String getUsage() { return "ex: underground"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public Object runCommand() {
	    expectNoMoreThan(1);
	    
		if (Envision.theWorld == null) {
			error("No world loaded!");
			return null;
		}
		
		//empty -- toggle
		if (noArgs()) {
			Envision.theWorld.setUnderground(!Envision.theWorld.isUnderground());
			return null;
		}
		
		String val = firstArg().toLowerCase();
		
		if (val.equals("true") || val.equals("t")) Envision.theWorld.setUnderground(true);
		else if (val.equals("false") || val.equals("f")) Envision.theWorld.setUnderground(false);
		else error(getUsage());
		
		return null;
	}
}

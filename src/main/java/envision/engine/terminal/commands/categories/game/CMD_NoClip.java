package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.game.entities.player.Player;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_NoClip extends TerminalCommand {
	
	public CMD_NoClip() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "noclip"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("nc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Allows player no-clipping"; }
	@Override public String getUsage() { return "ex: nc"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
	    
	    final Player p = Envision.thePlayer;
	    
	    if (p == null) {
	        error("There isn't a player!");
	        return;
	    }
	    
	    p.setNoClipAllowed(!p.isNoClipping());
        writeln(((p.isNoClipping()) ? "Enabled" : "Disabled") + " no clipping");
	}
	
}

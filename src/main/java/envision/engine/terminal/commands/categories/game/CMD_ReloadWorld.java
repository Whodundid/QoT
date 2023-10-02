package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.game.entities.player.Player;
import envision.game.world.GameWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;

public class CMD_ReloadWorld extends TerminalCommand {
	
	public CMD_ReloadWorld() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "reloadworld"; }
	@Override public EList<String> getAliases() { return EList.of("relw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads the current world instance"; }
	@Override public String getUsage() { return "ex: relw"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
		
		if (Envision.currentScreen instanceof MapEditorScreen editor) {
			writeln(EColors.yellow, "Reloading editor world '", editor.getActualWorld().getWorldName(), "..");
			editor.loadWorld();
			return;
		}
		
		if (Envision.theWorld == null) {
		    error("No world currently loaded!");
		    return;
		}
		
		writeln(EColors.yellow, "Reloading game world '", Envision.theWorld.getWorldName(), "'..");
        
        GameWorld w = Envision.loadWorld(new GameWorld(Envision.theWorld.getWorldFile()));
        Player p = Envision.setPlayer(new QoT_Player("Test"));
        w.addEntity(p);
        
        Envision.displayScreen(new GamePlayScreen());
	}
	
}

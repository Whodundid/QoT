package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.GameWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;

public class CMD_ReloadWorld extends TerminalCommand {
	
	public CMD_ReloadWorld() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "reloadworld"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("relw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads the current world instance"; }
	@Override public String getUsage() { return "ex: relw"; }
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.errorUsage(ERROR_NO_ARGS, getUsage());
			return;
		}
		
		if (Envision.currentScreen instanceof MapEditorScreen editor) {
			termIn.writeln(EColors.yellow, "Reloading editor world '", editor.getActualWorld().getWorldName(), "..");
			editor.loadWorld();
		}
		else if (Envision.theWorld != null) {
			termIn.writeln(EColors.yellow, "Reloading game world '", Envision.theWorld.getWorldName(), "'..");
			
			GameWorld w = Envision.loadWorld(new GameWorld(Envision.theWorld.getWorldFile()));
			QoT_Player p = (QoT_Player) Envision.setPlayer(new QoT_Player("Test"));
			w.addEntity(p);
			
			Envision.displayScreen(new GamePlayScreen());
		}
		else {
			termIn.error("No world currently loaded!");
		}
	}
	
}

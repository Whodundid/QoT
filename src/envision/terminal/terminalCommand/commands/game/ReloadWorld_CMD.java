package envision.terminal.terminalCommand.commands.game;

import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;
import game.entities.player.QoT_Player;
import game.screens.gameplay.GamePlayScreen;

public class ReloadWorld_CMD extends TerminalCommand {
	
	public ReloadWorld_CMD() {
		setCategory("Game");
		numArgs = 0;
	}

	@Override public String getName() { return "reloadworld"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("relw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads the current world instance"; }
	@Override public String getUsage() { return "ex: relw"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			termIn.errorUsage(ERROR_NO_ARGS, getUsage());
			return;
		}
		
		if (QoT.currentScreen instanceof MapEditorScreen editor) {
			termIn.writeln(EColors.yellow, "Reloading editor world '", editor.getActualWorld().getWorldName(), "..");
			editor.loadWorld();
		}
		else if (QoT.theWorld != null) {
			termIn.writeln(EColors.yellow, "Reloading game world '", QoT.theWorld.getWorldName(), "'..");
			
			GameWorld w = QoT.loadWorld(new GameWorld(QoT.theWorld.getWorldFile()));
			QoT_Player p = QoT.setPlayer(new QoT_Player("Test"));
			w.addEntity(p);
			
			QoT.displayScreen(new GamePlayScreen());
		}
		else {
			termIn.error("No world currently loaded!");
		}
	}
	
}

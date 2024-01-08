package envision.engine.terminal.commands.categories.game;

import java.io.File;

import envision.Envision;
import envision.engine.terminal.commands.categories.fileSystem.AbstractFileCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.GameWorld;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;
import qot.settings.QoTSettings;

public class CMD_LoadWorld extends AbstractFileCommand {
	
	public CMD_LoadWorld() {
		setCategory("Game");
		expectedArgLength = 0;
		setAcceptedModifiers("-ns");
	}

	@Override public String getName() { return "loadworld"; }
	@Override public EList<String> getAliases() { return EList.of("lw"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Loads the specified world"; }
	@Override public String getUsage() { return "ex: lw (world name)"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		fileTabComplete(termIn, QoTSettings.getEditorWorldsDir(), args);
	}
	
	@Override
	public void runCommand() {
	    expectExactly(1, "Error: Map name empty!");
		
		String worldName = firstArg();
		GameWorld world = buildWorld(worldName);
		
		boolean switchTo = !hasModifier("-ns");
		
		if (!world.getWorldFile().exists()) {
		    error("World '" + worldName + "' does not exist!");
		    return;
		}
		
		writeln("Loading world...", EColors.green);
        Envision.setPlayer(new QoT_Player("Test"));
        if (switchTo) Envision.displayScreen(new GamePlayScreen());
        Envision.loadLevel(world);
	}
	
	public static GameWorld buildWorld(String worldName) {
		File f = new File(QoTSettings.getEditorWorldsDir(), worldName);
		return new GameWorld(f);
	}
	
}
